package CNFSATSolver.CDCLSolver.SolverComponents.DataStructure.TwoWatchedLiteralsClassImplementations;

import CNFSATSolver.CDCLSolver.CoreClasses.AbstractImplementations.AbstractClause;
import CNFSATSolver.CDCLSolver.CoreClasses.*;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;
import CNFSATSolver.CoreComponents.Pair;
import java.util.ArrayList;

public class TwoWatchedLiteralsClause extends AbstractClause implements Clause {

    private int[] watchers = new int[2];
    private boolean isSingletonClause = false;
    private int learnedClauseIndexSecondHighestDecisionLevel = -1;

    public TwoWatchedLiteralsClause(int numVariables) {
        super(numVariables);
        watchers[0] = -1;
        watchers[1] = -1;
    }

    public void initializeClause() {

        if (clause.size() == 0) {
            return;
        }

        if (clause.size() == 1) {
            isSingletonClause = true;
            watchers[0] = 0;
            Literal lit1 = clause.get(watchers[0]);
            SingleVariableAssignment assignment1 = lit1.getVariable().getSingleVariableAssignment();
            addClauseWatcherOf(lit1, assignment1);
            return;
        }

        if (wasLearnedClause) {
            watchers[1] = learnedClauseIndexSecondHighestDecisionLevel;
            if (learnedClauseIndexSecondHighestDecisionLevel == 0) {
                watchers[0] = 1;
            } else {
                watchers[0] = 0;
            }

        } else {
            watchers[0] = 0;
            watchers[1] = (clause.size() - 1);
        }

        Literal lit1 = clause.get(watchers[0]);
        Literal lit2 = clause.get(watchers[1]);

        SingleVariableAssignment assignment1 = lit1.getVariable().getSingleVariableAssignment();
        SingleVariableAssignment assignment2 = lit2.getVariable().getSingleVariableAssignment();

        addClauseWatcherOf(lit1, assignment1);
        addClauseWatcherOf(lit2, assignment2);
    }

    private void addClauseWatcherOf(Literal lit, SingleVariableAssignment assignment) {

        if (lit.isNegated()) {
            assignment.addClauseThisVariableIsNegativeIn(this);
        } else {
            assignment.addClauseThisVariableIsPositiveIn(this);
        }

    }

    public void removeClause() {

        if (isSingletonClause) {
            removeWatcher(0);
            return;
        }
        removeWatcher(0);
        removeWatcher(1);
    }

    private void removeWatcher(int i) {
        Literal lit1 = clause.get(watchers[i]);
        SingleVariableAssignment assignment1 = lit1.getVariable().getSingleVariableAssignment();
        boolean isNegated = lit1.isNegated();

        if (isNegated) {
            assignment1.removeClauseThisVariableWasNegativeIn(this);
        } else {
            assignment1.removeClauseThisVariableWasPositiveIn(this);
        }
    }

    private void removeClauseWasWatcherOf(Literal lit, SingleVariableAssignment assignment) {

        if (lit.isNegated()) {
            assignment.removeClauseThisVariableWasNegativeIn(this);
        } else {
            assignment.removeClauseThisVariableWasPositiveIn(this);
        }

    }

    public Pair<Integer, Literal> getClauseStatus() {

        if (isSingletonClause) {
            return isSingletonClause();
        }

        Literal lit1 = clause.get(watchers[0]);
        Literal lit2 = clause.get(watchers[1]);

        SingleVariableAssignment assignment1 = lit1.getVariable().getSingleVariableAssignment();
        SingleVariableAssignment assignment2 = lit2.getVariable().getSingleVariableAssignment();

        boolean isClauseSatisfied = isTheClauseSatisfied(lit1, lit2, assignment1, assignment2);

        if (isClauseSatisfied) {
            return new Pair<Integer, Literal>(1, null);
        }

        Pair<Integer, Literal> statusWatcher0 = getClauseStatusHelper(0, 1, lit1, lit2, assignment1, assignment2);

        if (statusWatcher0.left != 0) {
            return statusWatcher0;
        }

        lit1 = clause.get(watchers[0]);
        assignment1 = lit1.getVariable().getSingleVariableAssignment();

        Pair<Integer, Literal> statusWatcher1 = getClauseStatusHelper(1, 0, lit2, lit1, assignment2, assignment1);

        return statusWatcher1;

    }

    private boolean isTheClauseSatisfied(Literal litA, Literal litB, SingleVariableAssignment assignmentA, SingleVariableAssignment assignmentB) {

        if (assignmentA.isAssigned()) {
            boolean value1 = assignmentA.getValue();
            if (value1 == !(litA.isNegated())) {
                return true;
            }
        }

        if (assignmentB.isAssigned()) {
            boolean value2 = assignmentB.getValue();
            if (value2 == !(litB.isNegated())) {
                return true;
            }
        }

        return false;
    }

    private Pair<Integer, Literal> getClauseStatusHelper(int watcherA, int watcherB, Literal litA, Literal litB,
                                      SingleVariableAssignment assignmentA, SingleVariableAssignment assignmentB) {

        if (assignmentA.isAssigned()) {

            if (assignmentA.getValue() != (litA.isNegated())) {
                return new Pair<Integer, Literal>(1, null);
            } else {

                boolean seenUnAssigned = false;
                Literal seenUnAssignedLit = null;
                for (int i = 0; i < clause.size(); i++) {

                    Literal lit = clause.get(i);
                    SingleVariableAssignment assignmentLit = lit.getVariable().getSingleVariableAssignment();

                    if (assignmentLit.isAssigned()) {
                        if (assignmentLit.getValue() == !(lit.isNegated())) {
                            if (i == watchers[watcherB]) {
                                return new Pair<Integer, Literal>(1, null);
                            } else {
                                watchers[watcherA] = i;
                                removeClauseWasWatcherOf(litA, assignmentA);
                                addClauseWatcherOf(lit, assignmentLit);
                                return new Pair<Integer, Literal>(1, null);
                            }
                        }

                    } else {
                        if (i != watchers[watcherB]) {
                            watchers[watcherA] = i;
                            removeClauseWasWatcherOf(litA, assignmentA);
                            addClauseWatcherOf(lit, assignmentLit);
                            return new Pair<Integer, Literal>(0, null);
                        } else {
                            seenUnAssigned = true;
                            seenUnAssignedLit = lit;
                        }
                    }
                }

                if (seenUnAssigned) {
                    return new Pair<Integer, Literal>(2, seenUnAssignedLit);
                } else {
                    return new Pair<Integer, Literal>(-1, null);
                }
            }
        }

        return new Pair<Integer, Literal>(0, null);
    }

    private Pair<Integer, Literal> isSingletonClause() {
        Literal lit = clause.get(0);
        SingleVariableAssignment assignment = lit.getVariable().getSingleVariableAssignment();
        if (assignment.isAssigned()) {
            boolean value = assignment.getValue();
            if (value == !(lit.isNegated())) {
                return new Pair<Integer, Literal>(1, null);
            } else {
                return new Pair<Integer, Literal>(-1, null);
            }

        } else {
            return new Pair<Integer, Literal>(2, lit);
        }
    }

    @Override
    public void wasLearnedClause(int learnedClauseIndexSecondHighestDecisionLevel) {
        this.wasLearnedClause = true;
        this.learnedClauseIndexSecondHighestDecisionLevel = learnedClauseIndexSecondHighestDecisionLevel;
    }

    @SuppressWarnings("unchecked")
    public Clause copy() {

        Clause ret = new TwoWatchedLiteralsClause(numVariables);
        ArrayList<Literal> literals = (ArrayList<Literal>) clause.clone();
        ret.setClauseList(literals);

        return ret;
    }
}
