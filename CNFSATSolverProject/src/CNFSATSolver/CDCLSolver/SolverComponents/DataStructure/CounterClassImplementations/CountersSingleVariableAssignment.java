package CNFSATSolver.CDCLSolver.SolverComponents.DataStructure.CounterClassImplementations;

import CNFSATSolver.CDCLSolver.CoreClasses.AbstractImplementations.AbstractSingleVariableAssignment;
import CNFSATSolver.CDCLSolver.CoreClasses.*;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;

import java.util.ArrayList;
import java.util.List;

public class CountersSingleVariableAssignment extends AbstractSingleVariableAssignment  implements SingleVariableAssignment {

    private List<Clause> clausesVariableAppearsPositivelyIn = new ArrayList<>();
    private List<Clause> clausesVariableAppearsNegativelyIn = new ArrayList<>();

    public CountersSingleVariableAssignment(Variable variable) {
        super(variable);
    }

    private void assign(boolean value) {
        if (value) {
            assignPositive();
        } else {
            assignNegative();
        }
    }

    private void assignPositive() {
        for (Clause clause : clausesVariableAppearsPositivelyIn) {
            clause.variableAssignedSatisfying(this);
        }
        for (Clause clause : clausesVariableAppearsNegativelyIn) {
            clause.variableAssignedNotSatisfying(this);
        }
    }

    private void assignNegative() {
        for (Clause clause : clausesVariableAppearsPositivelyIn) {
            clause.variableAssignedNotSatisfying(this);
        }
        for (Clause clause : clausesVariableAppearsNegativelyIn) {
            clause.variableAssignedSatisfying(this);
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
            clause.variableUnassignedSatisfying(this);
        }
        for (Clause clause : clausesVariableAppearsNegativelyIn) {
            clause.variableUnassignedNotSatisfying(this);
        }
    }

    private void unassignNegative() {
        for (Clause clause : clausesVariableAppearsPositivelyIn) {
            clause.variableUnassignedNotSatisfying(this);
        }
        for (Clause clause : clausesVariableAppearsNegativelyIn) {
            clause.variableUnassignedSatisfying(this);
        }
    }

    @Override
    public void wasChosen(boolean value, int decisionLevel, VariablesAssignments assignments) {
        assigned = true;
        chosen = true;
        antecedent = null;
        this.value = value;
        this.decisionLevel = decisionLevel;
        assignments.incrementNumVariablesAssigned();
        assign(value);
    }

    @Override
    public void wasUnitProp(boolean value, Clause antecedent, int decisionLevel, VariablesAssignments assignments) {
        assigned = true;
        wasUnitProp = true;
        this.value = value;
        this.decisionLevel = decisionLevel;
        this.antecedent = antecedent;
        if (antecedent.isLearnedClause()) {
            antecedent.lockClause();
        }
        assignments.incrementNumVariablesAssigned();
        assign(value);
    }

    @Override
    public void reset(VariablesAssignments assignments) {
        unassign();
        assigned = false;
        value = false;
        chosen = false;
        decisionLevel = -1;
        wasUnitProp = false;
        if (antecedent != null) {
            antecedent.unlockClause();
        }
        antecedent = null;
        assignments.decrementNumVariablesAssigned();
    }


    public void addClauseThisVariableIsPositiveIn(Clause clause) {
        clausesVariableAppearsPositivelyIn.add(clause);
    }

    public void addClauseThisVariableIsNegativeIn(Clause clause) {
        clausesVariableAppearsNegativelyIn.add(clause);
    }

    public List<Clause> getClausesThisVariablePositiveIn() {
        return clausesVariableAppearsPositivelyIn;
    }

    public List<Clause> getClausesThisVariableNegativeIn() {
        return clausesVariableAppearsNegativelyIn;
    }

    public void removeClauseThisVariableWasPositiveIn(Clause c) {
        clausesVariableAppearsPositivelyIn.remove(c);
    }

    public void removeClauseThisVariableWasNegativeIn(Clause c) {
        clausesVariableAppearsNegativelyIn.remove(c);
    }

}


