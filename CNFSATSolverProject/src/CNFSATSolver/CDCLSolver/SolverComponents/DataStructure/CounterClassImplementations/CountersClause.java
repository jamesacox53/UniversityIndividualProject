package CNFSATSolver.CDCLSolver.SolverComponents.DataStructure.CounterClassImplementations;

import CNFSATSolver.CDCLSolver.CoreClasses.AbstractImplementations.AbstractClause;
import CNFSATSolver.CDCLSolver.CoreClasses.*;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;
import CNFSATSolver.CoreComponents.Pair;
import java.util.ArrayList;

public class CountersClause extends AbstractClause implements Clause {

    private int numLiteralsCurrSatisfyClauseCounter = 0;
    private int numLiteralsCurrDontSatisfyClauseCounter = 0;

    public CountersClause(int numVariables) {
        super(numVariables);
    }

    public void variableAssignedSatisfying(SingleVariableAssignment assignment) {
        numLiteralsCurrSatisfyClauseCounter++;
    }

    public void variableAssignedNotSatisfying(SingleVariableAssignment assignment) {
        numLiteralsCurrDontSatisfyClauseCounter++;
    }

    public void variableUnassignedSatisfying(SingleVariableAssignment assignment) {
        numLiteralsCurrSatisfyClauseCounter--;
    }

    public void variableUnassignedNotSatisfying(SingleVariableAssignment assignment) {
        numLiteralsCurrDontSatisfyClauseCounter--;
    }

    public void initializeClause() {

        for (Literal literal : clause) {

            SingleVariableAssignment assignment = literal.getVariable().getSingleVariableAssignment();
            boolean isNegated = literal.isNegated();

            if(isNegated) {
                assignment.addClauseThisVariableIsNegativeIn(this);
            } else {
                assignment.addClauseThisVariableIsPositiveIn(this);
            }
        }
    }

    public void removeClause() {

        for (Literal literal : clause) {

            SingleVariableAssignment assignment = literal.getVariable().getSingleVariableAssignment();
            boolean isNegated = literal.isNegated();

            if(isNegated) {
                assignment.removeClauseThisVariableWasNegativeIn(this);
            } else {
                assignment.removeClauseThisVariableWasPositiveIn(this);
            }
        }
    }

    public Pair<Integer, Literal> getClauseStatus() {

        if (isSatisfied()) {
            return new Pair<Integer, Literal>(1, null);

        } else if (isUnsatisfied()) {
            return new Pair<Integer, Literal>(-1, null);

        } else if (isUnitClause()) {

            for (Literal lit : clause) {

                boolean isAssigned = lit.getVariable().getSingleVariableAssignment().isAssigned();
                if (!isAssigned) {
                    return new Pair<Integer, Literal>(2, lit);
                }
            }
            return null;

        } else {
            return new Pair<Integer, Literal>(0, null);
        }
    }

    private boolean isSatisfied() {
        return numLiteralsCurrSatisfyClauseCounter > 0;
    }

    private boolean isUnsatisfied() {
        return (numLiteralsCurrDontSatisfyClauseCounter >= clause.size());
    }

    private boolean isUnitClause() {
        return (numLiteralsCurrDontSatisfyClauseCounter == clause.size() - 1);
    }

    public void wasLearnedClause(int learnedClauseIndexSecondHighestDecisionLevel) {
        this.wasLearnedClause = true;
    }

    @SuppressWarnings("unchecked")
    public Clause copy() {

        Clause ret = new CountersClause(numVariables);
        ArrayList<Literal> literals = (ArrayList<Literal>) clause.clone();
        ret.setClauseList(literals);

        return ret;
    }

}
