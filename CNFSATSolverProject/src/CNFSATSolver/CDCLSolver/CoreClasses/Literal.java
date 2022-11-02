package CNFSATSolver.CDCLSolver.CoreClasses;

import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;
import CNFSATSolver.CoreComponents.Interfaces.LiteralI;

public class Literal implements LiteralI {
    private final Variable variable;
    private final boolean isNegated;
    private double activity = 0;

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

    public double getActivity() {
        return activity;
    }

    public void decayActivity(double decayFactor) {
        activity *= decayFactor;
    }

    public void bumpActivity(double bumpActivity) {
        activity += bumpActivity;
    }

    public void setActivity(double activity) {
        this.activity = activity;
    }

    @Override
    public String toString() {
        if (isNegated) {
            return Integer.toString(-variable.getVariable());
        }
        return Integer.toString(variable.getVariable());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Literal)) {
            return false;
        }

        Literal litOther = (Literal) obj;
        return (litOther.getVariable().getVariable() == variable.getVariable()) && (isNegated == litOther.isNegated());
    }

    @Override
    public int hashCode() {
        return variable.hashCode();
    }
}
