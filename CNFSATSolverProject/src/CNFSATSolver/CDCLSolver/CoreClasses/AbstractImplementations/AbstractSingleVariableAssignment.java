package CNFSATSolver.CDCLSolver.CoreClasses.AbstractImplementations;

import CNFSATSolver.CDCLSolver.CoreClasses.*;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;

import java.util.List;

public abstract class AbstractSingleVariableAssignment implements SingleVariableAssignment {

    protected final Variable variable;
    protected boolean value = false;
    protected boolean assigned = false;
    protected boolean chosen = false;
    protected boolean wasUnitProp = false;
    protected Clause antecedent = null;
    protected int decisionLevel = -1;

    public AbstractSingleVariableAssignment(Variable variable) {
        this.variable = variable;
    }

    public abstract void wasChosen(boolean value, int decisionLevel, VariablesAssignments assignments);

    public abstract void wasUnitProp(boolean value, Clause antecedent, int decisionLevel, VariablesAssignments assignments);

    public abstract void reset(VariablesAssignments assignments);

    public Variable getVariable() {
        return variable;
    }

    public boolean getValue() {
        return value;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public boolean isChosen() {
        return chosen;
    }

    public boolean isUnitProp() {
        return wasUnitProp;
    }

    public Clause getAntecedent() { return antecedent; }

    public int getDecisionLevel() {return decisionLevel; }

    public abstract void addClauseThisVariableIsPositiveIn(Clause clause);

    public abstract void addClauseThisVariableIsNegativeIn(Clause clause);

    public abstract List<Clause> getClausesThisVariablePositiveIn();

    public abstract List<Clause> getClausesThisVariableNegativeIn();

    public abstract void removeClauseThisVariableWasPositiveIn(Clause c);

    public abstract void removeClauseThisVariableWasNegativeIn(Clause c);

    @Override
    public String toString() {
        return "var:" + variable.toString() + " a:" + assigned + " val:" + value + " c:" + chosen;
    }
}


