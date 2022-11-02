package CNFSATSolver.CDCLSolver.SolverComponents.DataStructure.TwoWatchedLiteralsClassImplementations;

import CNFSATSolver.CDCLSolver.CoreClasses.AbstractImplementations.AbstractSingleVariableAssignment;
import CNFSATSolver.CDCLSolver.CoreClasses.*;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;

import java.util.ArrayList;
import java.util.List;

public class TwoWatchedLiteralsSingleVariableAssignment extends AbstractSingleVariableAssignment implements SingleVariableAssignment {

    private List<Clause> watchersOfClausesVariableAppearsPositivelyIn = new ArrayList<>();
    private List<Clause> watchersOfClausesVariableAppearsNegativelyIn = new ArrayList<>();

    public TwoWatchedLiteralsSingleVariableAssignment(Variable variable) {
        super(variable);
    }

    @Override
    public void wasChosen(boolean value, int decisionLevel, VariablesAssignments assignments) {
        assigned = true;
        chosen = true;
        antecedent = null;
        this.value = value;
        this.decisionLevel = decisionLevel;
        assignments.incrementNumVariablesAssigned();
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
    }

    @Override
    public void reset(VariablesAssignments assignments) {
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

    @Override
    public void addClauseThisVariableIsPositiveIn(Clause clause) {
        watchersOfClausesVariableAppearsPositivelyIn.add(clause);
    }

    @Override
    public void addClauseThisVariableIsNegativeIn(Clause clause) {
        watchersOfClausesVariableAppearsNegativelyIn.add(clause);
    }

    @Override
    public List<Clause> getClausesThisVariablePositiveIn() {
        return watchersOfClausesVariableAppearsPositivelyIn;
    }

    @Override
    public List<Clause> getClausesThisVariableNegativeIn() {
        return watchersOfClausesVariableAppearsNegativelyIn;
    }

    @Override
    public void removeClauseThisVariableWasPositiveIn(Clause c) {
        watchersOfClausesVariableAppearsPositivelyIn.remove(c);
    }

    @Override
    public void removeClauseThisVariableWasNegativeIn(Clause c) {
        watchersOfClausesVariableAppearsNegativelyIn.remove(c);
    }
}


