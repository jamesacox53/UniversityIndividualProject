package CNFSATSolver.DPLLSolver;

import CNFSATSolver.CoreComponents.Interfaces.LiteralI;

public class Literal  implements LiteralI{
    private final Variable variable;
    private final boolean isNegated;

    public Literal(Variable variable, boolean isNegated) {

        this.variable = variable;
        this.isNegated = isNegated;
    }

    public Variable getVariable() {
        return variable;
    }

    public boolean isNegated() {
        return isNegated;
    }

    @Override
    public String toString() {
        if (isNegated) {
            return Integer.toString(-variable.getVariable());
        }
        return Integer.toString(variable.getVariable());
    }
}
