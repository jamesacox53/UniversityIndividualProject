package CNFSATSolver.Solvers;

import CNFSATSolver.DPLLSolver.*;
import CNFSATSolver.CoreComponents.Interfaces.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DPLLSolver implements SATSolver {

    private long timeStartSolving = 0;
    private boolean fiveMinTimeOut = false;
    private boolean overallTimeOut = false;
    private long fiveMinTimeOutTime = 0;
    private long overallTimeOutTime = 0;

    public VariablesAssignments solve(SatProblem satProblem) {

        setSolveStartTime();

        if (fiveMinTimeOut) {
            setFiveMinsTimeOutTime();
        }

        SatProblemForDPLL dpllSatProblem = (SatProblemForDPLL) satProblem;

        CnfFormula cnfFormula = dpllSatProblem.left;
        VariablesAssignments assignments = dpllSatProblem.right;

        int currentDecisionLevel = 0;

        while (currentDecisionLevel >= 0) {

            if (timeout()) {
                return timedOut(assignments);
            }

            if (currentDecisionLevel < assignments.getMaxCurrDecisionLevel()) {
                // We Have BackTracked
                backTracked(currentDecisionLevel, assignments);

                SingleVariableAssignment assignment = assignments.getChosenAssignmentAtDecisionLevel(currentDecisionLevel);
                if (assignment == null) {
                    return null;
                }

                boolean value = assignment.getValue();

                if (!value) {
                    assignment.reset();
                    assignment.wasChosen(!value);
                    currentDecisionLevel++;
                } else {
                    //backtrack again
                    currentDecisionLevel = backTrack(currentDecisionLevel);
                }

            } else {

                    assignments.createdDecisionLevel(currentDecisionLevel);

                    unitPropogation(cnfFormula, currentDecisionLevel, assignments);

                    pureLiteralElimination(cnfFormula, currentDecisionLevel, assignments);

                    int isSatisfied = cnfFormula.isSatisfied(assignments);

                    if (isSatisfied == 1) {
                        return solveable(assignments);

                    } else if (isSatisfied == 0) {
                        // assign a variable if there is one

                        SingleVariableAssignment assignment = assignments.getNextUnassignedVariableAssignment();

                        if (assignment == null) {
                            return null;
                        }

                        assignment.wasChosen(false);
                        assignments.addDecisionAtDecisionLevel(currentDecisionLevel, assignment);

                        currentDecisionLevel++;

                    } else if (isSatisfied == -1) {
                        // we need to backtrack
                        currentDecisionLevel = backTrack(currentDecisionLevel);
                    } else {
                        return null;
                    }


                }
            }
    // the formula is unsatisfiable
        return notSolveable(assignments);
    }

    public static int backTrack(int currDecisionLevel) {

        return currDecisionLevel - 1;
    }

    public static void backTracked(int currDecisionLevel, VariablesAssignments assignments) {
        assignments.removeDecisionLevel(assignments.getMaxCurrDecisionLevel(), currDecisionLevel);
    }

    public static void unitPropogation(CnfFormula cnfFormula, int decisionLevel, VariablesAssignments assignments) {
        boolean hasUnitClause = false;

        do {
            hasUnitClause = false;
            SingleVariableAssignment unitClause = cnfFormula.getUnitClause(assignments);

            if (unitClause != null) {
                assignments.addNonDecisionAtDecisionLevel(decisionLevel, unitClause);
                hasUnitClause = true;
            }

        } while (hasUnitClause);

    }

    public static void pureLiteralElimination(CnfFormula cnfFormula, int decisionLevel, VariablesAssignments assignments) {
        boolean hasPureLiteralElimination = false;
        do {
            hasPureLiteralElimination = false;
            Variable[] variables = assignments.getVariables();
            for (Variable variable : variables) {
                SingleVariableAssignment assignment = variable.getSingleVariableAssignment();
                if(!assignment.isAssigned()) {
                    List<Clause> clausesAppearPositivelyIn = assignment.getClausesVariableAppearsPositivelyIn();
                    List<Clause> clausesAppearNegativelyIn = assignment.getClausesVariableAppearsNegativelyIn();

                    boolean appearsPositive = false;
                    boolean appearsNegative = false;

                    for (int i = 0; !appearsPositive && i < clausesAppearPositivelyIn.size(); i++) {
                        Clause clause = clausesAppearPositivelyIn.get(i);

                        if (!clause.isSatisfied()) {
                            appearsPositive = true;
                        }
                    }

                    for (int i = 0; !appearsNegative && i < clausesAppearNegativelyIn.size(); i++) {
                        Clause clause = clausesAppearNegativelyIn.get(i);

                        if (!clause.isSatisfied()) {
                            appearsNegative = true;
                        }
                    }

                    if (appearsPositive && !appearsNegative) {
                        assignment.wasPureLiteralElimination(true);
                        assignments.addNonDecisionAtDecisionLevel(decisionLevel, assignment);
                        hasPureLiteralElimination = true;

                    } else if (!appearsPositive && appearsNegative) {
                        assignment.wasPureLiteralElimination(false);
                        assignments.addNonDecisionAtDecisionLevel(decisionLevel, assignment);
                        hasPureLiteralElimination = true;
                    }
                }
            }
        } while (hasPureLiteralElimination);
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

}
