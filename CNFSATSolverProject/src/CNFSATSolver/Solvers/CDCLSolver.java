package CNFSATSolver.Solvers;

import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;
import CNFSATSolver.CoreComponents.Interfaces.SatProblem;
import CNFSATSolver.CoreComponents.Pair;
import CNFSATSolver.CDCLSolver.CoreClasses.*;
import CNFSATSolver.CDCLSolver.SolverComponents.ClauseDeletion.ClauseDeletion;
import CNFSATSolver.CDCLSolver.SolverComponents.RestartPolicy.RestartPolicy;
import CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher.VariablePicker;


import java.util.*;
import java.util.concurrent.TimeUnit;

public class CDCLSolver implements SATSolver {

    private VariablePicker variablePicker;
    private RestartPolicy restartPolicy;
    private ClauseDeletion clauseDeletionPolicy;
    private long timeStartSolving = 0;
    private boolean fiveMinTimeOut = false;
    private boolean overallTimeOut = false;
    private long fiveMinTimeOutTime = 0;
    private long overallTimeOutTime = 0;

    public CDCLSolver(VariablePicker variablePicker, RestartPolicy restartPolicy, ClauseDeletion clauseDeletionPolicy) {

        this.variablePicker = variablePicker;
        this.restartPolicy = restartPolicy;
        this.clauseDeletionPolicy = clauseDeletionPolicy;
    }

    public VariablesAssignments solve(SatProblem satProblem) {

        setSolveStartTime();

        if (fiveMinTimeOut) {
            setFiveMinsTimeOutTime();
        }

        SatProblemForCDCL cdclSatProblem = (SatProblemForCDCL) satProblem;

        CnfFormula cnfFormula = cdclSatProblem.left;
        VariablesAssignments assignments = cdclSatProblem.right;

        LinkedList<Clause> queue = new LinkedList<>();
        HashSet<Clause> inQueue = new HashSet<>();
        int numConflicts = 0;

        assignments.createDecisionLevel(0);

        variablePicker.initialise(cnfFormula);

        Clause conflictClause = preprocess(queue, inQueue, cnfFormula, 0, assignments);
        if (conflictClause != null) {
            return notSolveable(assignments);
        }

        int currentDecisionLevel = 0;
        boolean backtracked = false;

        while (!assignments.allVariablesAssigned()) {

            if (timeout()) {
                return timedOut(assignments);
            }

            if (!backtracked) {

                runPeriodicFunctions(numConflicts);

                boolean restarted = restart(numConflicts, assignments);

                if (restarted) {
                    currentDecisionLevel = 0;
                }

                Literal literal = variablePicker.getNextLiteral();

                SingleVariableAssignment assignment = literal.getVariable().getSingleVariableAssignment();
                boolean value = !literal.isNegated();

                currentDecisionLevel += 1;
                assignments.createDecisionLevel(currentDecisionLevel);

                assignChosen(assignment, value, currentDecisionLevel, assignments);

                addUnsatisfiedWatchedClausesToQueue(queue, inQueue, assignment, value);
            }

            backtracked = false;

            conflictClause = unitPropogation(queue, inQueue, currentDecisionLevel, assignments);

            if (conflictClause != null) {
                if (currentDecisionLevel == 0) {
                    return notSolveable(assignments);
                }

                Pair<Clause, Integer> analysis = conflictAnalysis(currentDecisionLevel, conflictClause, cnfFormula, assignments);
                Clause learnedClause = analysis.left;
                int newDecisionLevel = analysis.right;
                cnfFormula.addLearntClause(learnedClause);

                numConflicts++;

                queue.clear();
                inQueue.clear();

                inQueue.add(learnedClause);
                queue.add(learnedClause);

                decayClauseActivities();

                currentDecisionLevel = newDecisionLevel;
                backTrack(currentDecisionLevel, assignments);
                backtracked = true;
            }
        }

        return solveable(assignments);
    }

    private Clause preprocess(LinkedList<Clause> queue, HashSet<Clause> inQueue, CnfFormula cnfFormula, int decisionLevel, VariablesAssignments assignments) {

        List<Clause> problemConstraintClauses = cnfFormula.getProblemConstraintClauses();
        List<Clause> learntClauses = cnfFormula.getLearntClauses();

        for (Clause clause : problemConstraintClauses) {
            queue.add(clause);
            inQueue.add(clause);
        }

        for (Clause clause : learntClauses) {
            queue.add(clause);
            inQueue.add(clause);
        }

        return unitPropogation(queue, inQueue, decisionLevel, assignments);
    }

    private void assignChosen(SingleVariableAssignment assignment, boolean value, int currentDecisionLevel, VariablesAssignments assignments) {

        assignment.wasChosen(value, currentDecisionLevel, assignments);
        assignments.addDecisionAtDecisionLevel(currentDecisionLevel, assignment);

        variablePicker.removeVariable(assignment.getVariable());
    }

    private void assignPropagated(SingleVariableAssignment assignment, boolean value, Clause antecedent, int decisionLevel, VariablesAssignments assignments) {

        assignment.wasUnitProp(value, antecedent, decisionLevel, assignments);
        assignments.addNonDecisionAtDecisionLevel(decisionLevel, assignment);

        variablePicker.removeVariable(assignment.getVariable());
    }

    private Pair<Clause, Integer> conflictAnalysis(int maxDecisionLevel, Clause conflictClause, CnfFormula cnfFormula, VariablesAssignments assignments) {

        Clause learnedClause = conflictClause.copy();
        List<SingleVariableAssignment> listAssignments = assignments.getPropagatedAssignmentsAtDecisionLevel(maxDecisionLevel);
        boolean stopped = false;
        for (int i = listAssignments.size() - 1; (!stopped) && (i >= 0); i--) {
            if (learnedClause.getNumVariablesAtDecisionLevel(maxDecisionLevel) <= 1) {
                stopped = true;
            } else {
                SingleVariableAssignment assignment = listAssignments.get(i);
                if (learnedClause.containsVariable(assignment.getVariable())) {
                    learnedClause.resolve(assignment.getVariable());

                    Clause antecedent = assignment.getAntecedent();
                    if (antecedent.isLearnedClause()) {
                        clauseDeletionPolicy.bumpClauseActivity(antecedent);
                    }
                }
            }
        }

        ArrayList<Literal> list = learnedClause.getClause();
        int indexLiteralSecondHighestDecisionLevel = list.size() - 1;
        int retNewDecisionLevel = 0;

        for (int i = 0; i < list.size(); i++) {

            Literal lit = list.get(i);

            SingleVariableAssignment assignment = lit.getVariable().getSingleVariableAssignment();

            learnedClause.variableAssignedNotSatisfying(assignment);

            int decisionLevel = assignment.getDecisionLevel();

            if (decisionLevel < maxDecisionLevel) {
                if (decisionLevel > retNewDecisionLevel) {
                    retNewDecisionLevel = decisionLevel;
                    indexLiteralSecondHighestDecisionLevel = i;
                }
            }
        }

        learnedClause.wasLearnedClause(indexLiteralSecondHighestDecisionLevel);

        clauseDeletionPolicy.bumpClauseActivity(learnedClause);
        variablePicker.bumpActivities(list);

        return (new Pair<Clause, Integer>(learnedClause, retNewDecisionLevel));
    }


    private VariablesAssignments notSolveable(VariablesAssignments assignments) {

        long timeSpentSolving = getTimeSpentSolving();
        assignments.setSolveable(false, timeSpentSolving);
        return assignments;
    }

    private VariablesAssignments solveable(VariablesAssignments assignments) {

        long timeSpentSolving = getTimeSpentSolving();
        assignments.setSolveable(true, timeSpentSolving);
        return assignments;
    }

    private void backTrack(int currDecisionLevel, VariablesAssignments assignments) {
        assignments.removeAllAboveDecisionLevels(currDecisionLevel, variablePicker);
    }

    private void decayLiterals(int numConflicts) {
        variablePicker.decayLiterals(numConflicts);
    }

    private boolean restart(int numConflicts, VariablesAssignments variablesAssignments) {
        return restartPolicy.restart(numConflicts, variablePicker, variablesAssignments);
    }

    private void decayClauseActivities() {
        clauseDeletionPolicy.decayClauseActivities();
    }

    private void runPeriodicFunctions(int numConflicts) {
        decayLiterals(numConflicts);
        clauseDeletionPolicy.deleteClauses();
    }

    private Clause unitPropogation(LinkedList<Clause> queue, HashSet<Clause> inQueue, int decisionLevel, VariablesAssignments assignments) {

        while (!queue.isEmpty()) {

            Clause clause = queue.remove();
            inQueue.remove(clause);

            Pair<Integer, Literal> clauseStatus = clause.getClauseStatus();

            if (clauseStatus.left == -1) {
                return clause;
            }

            if (clauseStatus.left == 2) {
                Literal lit = clauseStatus.right;
                SingleVariableAssignment assignment = lit.getVariable().getSingleVariableAssignment();
                boolean value = !lit.isNegated();

                assignPropagated(assignment, value, clause, decisionLevel, assignments);

                addUnsatisfiedWatchedClausesToQueue(queue, inQueue, assignment, value);
            }
        }

        return null;
    }

    private void addUnsatisfiedWatchedClausesToQueue(LinkedList<Clause> queue, HashSet<Clause> inQueue, SingleVariableAssignment assignment, boolean value) {

        List<Clause> clausesWatchedNotSatisfied;

        if (value) {
            clausesWatchedNotSatisfied = assignment.getClausesThisVariableNegativeIn();
        } else {
            clausesWatchedNotSatisfied = assignment.getClausesThisVariablePositiveIn();
        }

        for (Clause clauseIter : clausesWatchedNotSatisfied) {

            if (!inQueue.contains(clauseIter)) {
                inQueue.add(clauseIter);
                queue.add(clauseIter);
            }
        }
    }

    private void setFiveMinsTimeOutTime() {

        long fiveMinsInFuture = timeStartSolving + TimeUnit.MINUTES.toMillis(5);
        fiveMinTimeOutTime = fiveMinsInFuture;
    }

    public void setFiveMinTimeOut() {
        fiveMinTimeOut = true;
    }

    public void setOverallTimeOutTime(long overallTime) {
        overallTimeOut = true;
        overallTimeOutTime = overallTime;
    }

    private boolean timeout() {

        if (fiveMinTimeOut || overallTimeOut) {

            long currTime = System.currentTimeMillis();

            if (fiveMinTimeOut) {
                if (currTime >= fiveMinTimeOutTime) {
                    return true;
                }
            }

            if (overallTimeOut) {

                if (currTime >= overallTimeOutTime) {
                    return true;
                }
            }
        }

        return false;
    }

    private VariablesAssignments timedOut(VariablesAssignments assignments) {
        long timeSpentSolving = getTimeSpentSolving();

        assignments.setTimedOut(timeSpentSolving);
        return assignments;
    }

    private long getTimeSpentSolving() {
        return System.currentTimeMillis() - timeStartSolving;
    }

    private void setSolveStartTime() {
        timeStartSolving = System.currentTimeMillis();
    }

}
