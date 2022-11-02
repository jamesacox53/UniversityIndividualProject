package CNFSATSolver.DPLLSolver;

import CNFSATSolver.CoreComponents.Interfaces.SingleVariableAssignmentI;
import java.util.ArrayList;
import java.util.List;

public class SingleVariableAssignment implements SingleVariableAssignmentI {

    private final Variable variable;
    private boolean value = false;
    private boolean assigned = false;
    private boolean chosen = false;
    private boolean wasUnitProp = false;
    private boolean wasPureLiteralElimination = false;
    private List<Clause> clausesVariableAppearsPositivelyIn = new ArrayList<>();
    private List<Clause> clausesVariableAppearsNegativelyIn = new ArrayList<>();

    private void assign(boolean value) {
        if (value) {
            assignPositive();
        } else {
            assignNegative();
        }
    }

    private void assignPositive() {
        for (Clause clause : clausesVariableAppearsPositivelyIn) {
            clause.incrementNumLiteralsSatisfy();
        }
        for (Clause clause : clausesVariableAppearsNegativelyIn) {
            clause.incrementNumLiteralsDontSatisfy();
        }
    }

    private void assignNegative() {
        for (Clause clause : clausesVariableAppearsPositivelyIn) {
            clause.incrementNumLiteralsDontSatisfy();
        }
        for (Clause clause : clausesVariableAppearsNegativelyIn) {
            clause.incrementNumLiteralsSatisfy();
        }
    }

    private void unassign() {
        if (this.value) {
            unassignPositive();
        } else {
            unassignNegative();
        }
    }

    private void unassignPositive() {
        for (Clause clause : clausesVariableAppearsPositivelyIn) {
            clause.decrementNumLiteralsSatisfy();
        }
        for (Clause clause : clausesVariableAppearsNegativelyIn) {
            clause.decrementNumLiteralsDontSatisfy();
        }
    }

    private void unassignNegative() {
        for (Clause clause : clausesVariableAppearsPositivelyIn) {
            clause.decrementNumLiteralsDontSatisfy();
        }
        for (Clause clause : clausesVariableAppearsNegativelyIn) {
            clause.decrementNumLiteralsSatisfy();
        }
    }

    public SingleVariableAssignment(Variable variable) {
        this.variable = variable;
    }

    public void wasChosen(boolean value) {
        assigned = true;
        chosen = true;
        this.value = value;
        assign(value);
    }

    public void wasUnitProp(boolean value) {
        assigned = true;
        wasUnitProp = true;
        this.value = value;
        assign(value);
    }

    public void wasPureLiteralElimination(boolean value) {
        assigned = true;
        wasPureLiteralElimination = true;
        this.value = value;
        assign(value);
    }

    public void reset() {
        unassign();
        assigned = false;
        value = false;
        chosen = false;
        wasUnitProp = false;
        wasPureLiteralElimination = false;
    }

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

    public boolean isPureLiteralElimination() {
        return wasPureLiteralElimination;
    }

    public void addClauseVariableAppearsPositivelyIn(Clause clause) {
        clausesVariableAppearsPositivelyIn.add(clause);
    }

    public void addClauseVariableAppearsNegativelyIn(Clause clause) {
        clausesVariableAppearsNegativelyIn.add(clause);
    }

    public List<Clause> getClausesVariableAppearsPositivelyIn() {
        return clausesVariableAppearsPositivelyIn;
    }

    public List<Clause> getClausesVariableAppearsNegativelyIn() {
        return clausesVariableAppearsNegativelyIn;
    }

    @Override
    public String toString() {
        return "var:" + variable.toString() + " a:" + assigned + " val:" + value + " c:" + chosen;
    }
}


