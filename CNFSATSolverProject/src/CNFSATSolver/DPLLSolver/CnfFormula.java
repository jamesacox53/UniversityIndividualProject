package CNFSATSolver.DPLLSolver;

import java.util.ArrayList;
import java.util.List;

public class CnfFormula {

    private final List<Clause> formula = new ArrayList<>();
    private final int numVariables;
    private final int numClauses;

    public CnfFormula(int numVariables, int numClauses) {
        this.numVariables = numVariables;
        this.numClauses = numClauses;
    }

    public void addClause(Clause clause) {
        formula.add(clause);
    }

    public int isSatisfied(VariablesAssignments assignments) {
        boolean wasUnresolved = false;
        for (Clause clause : formula) {

            if (clause.isUnSatisfied()) {
                return -1;
            }
            else if (!clause.isSatisfied()) {
                wasUnresolved = true;
            }
        }
        if (wasUnresolved) {
            return 0;
        }

        return 1;
    }

    public SingleVariableAssignment getUnitClause(VariablesAssignments assignments) {
        for (Clause clause : formula) {
            if (clause.isUnSatisfied()) {
                return null;
            }

            if (clause.isUnitClause()) {
                List<Literal> literals = clause.getClause();
                for (Literal lit : literals) {
                    SingleVariableAssignment assignment = lit.getVariable().getSingleVariableAssignment();
                    if (!assignment.isAssigned()) {
                        assignment.wasUnitProp(!lit.isNegated());
                        return assignment;
                    }
                }
            }
        }
        return null;
    }

    public List<Clause> getFormula() {
        return formula;
    }

    public int getNumVariables() {
        return numVariables;
    }

    public int getNumClauses() {
        return numClauses;
    }

    public int getSizeFormula() {
        return formula.size();
    }

    public Clause getClause(int index) {
        return formula.get(index);
    }
}
