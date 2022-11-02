package CNFSATSolver.CDCLSolver.CoreClasses.Interfaces;

import CNFSATSolver.CDCLSolver.CoreClasses.Literal;
import CNFSATSolver.CoreComponents.Interfaces.*;
import CNFSATSolver.CoreComponents.Pair;

import java.util.ArrayList;

public interface Clause extends ClauseI {

    public void variableAssignedSatisfying(SingleVariableAssignment assignment);

    public void variableAssignedNotSatisfying(SingleVariableAssignment assignment);

    public void variableUnassignedSatisfying(SingleVariableAssignment assignment);

    public void variableUnassignedNotSatisfying(SingleVariableAssignment assignment);

    public void setClauseList(ArrayList<Literal> clause);

    public void setClauseNumber(int num);

    public double getActivity();

    public int getClauseNumber();

    public void lockClause();

    public void unlockClause();

    public void resolve(Variable variable);

    public int getNumVariablesAtDecisionLevel(int decisionLevel);

    public void addLiteral(Literal literal);

    public void initializeClause();

    public void removeClause();

    public void decayActivity(double decayFactor);

    public void bumpActivity(double bumpActivity);

    public void setActivity(double activity) ;

    public ArrayList<Literal> getClause();

    public int getNumVariables();

    public Pair<Integer, Literal> getClauseStatus();

    public void wasLearnedClause(int learnedClauseIndexSecondHighestDecisionLevel);

    public Clause copy();

    public boolean containsVariable(Variable variable);

    public boolean isLearnedClause();

    public boolean isLocked();

    public int getSize();

    public String toString();

    public int hashCode();

    public boolean equals(Object obj);

    public int getNumLiterals();

    public Literal getLitNum(int index);

}
