package CNFSATSolver.DPLLSolver;

import CNFSATSolver.CoreComponents.Interfaces.VariableI;

public class Variable implements VariableI {

    private final int variable;
    private final SingleVariableAssignment singleVariableAssignment;

    public Variable(int variable) {
        this.variable = variable;
        this.singleVariableAssignment = new SingleVariableAssignment(this);
    }

    public int getVariable() {
        return variable;
    }

    public SingleVariableAssignment getSingleVariableAssignment() {
        return singleVariableAssignment;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Variable)) {
            return false;
        }

        Variable varOther = (Variable) obj;
        return varOther.getVariable() == variable;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(variable);
    }

    @Override
    public String toString() {
        return Integer.toString(variable);
    }
}
