package CNFSATSolver.CDCLSolver.CoreClasses.Interfaces;

import CNFSATSolver.CDCLSolver.CoreClasses.VariablesAssignments;
import CNFSATSolver.CoreComponents.Interfaces.*;

import java.util.List;

public interface SingleVariableAssignment extends SingleVariableAssignmentI {

    public void wasChosen(boolean value, int decisionLevel, VariablesAssignments assignments);

    public void wasUnitProp(boolean value, Clause antecedent, int decisionLevel, VariablesAssignments assignments);

    public void reset(VariablesAssignments assignments);

    public Variable getVariable();

    public boolean getValue();

    public boolean isAssigned();

    public boolean isChosen();

    public boolean isUnitProp();

    public Clause getAntecedent();

    public int getDecisionLevel();

    public void addClauseThisVariableIsPositiveIn(Clause clause);

    public void addClauseThisVariableIsNegativeIn(Clause clause);

    public List<Clause> getClausesThisVariablePositiveIn();

    public List<Clause> getClausesThisVariableNegativeIn();

    public void removeClauseThisVariableWasPositiveIn(Clause c);

    public void removeClauseThisVariableWasNegativeIn(Clause c);

    public String toString();

}
