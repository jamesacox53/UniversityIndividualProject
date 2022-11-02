package CNFSATSolver.CDCLSolver.CoreClasses.AbstractImplementations;

import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;

public abstract class AbstractVariable implements Variable {

    protected final int variable;
    protected final SingleVariableAssignment singleVariableAssignment;

    public AbstractVariable(int variable) {
        this.variable = variable;
        this.singleVariableAssignment = newSingleVariableAssignment();
    }

    protected abstract SingleVariableAssignment newSingleVariableAssignment();

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
