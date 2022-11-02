package CNFSATSolver.CDCLSolver.CoreClasses.AbstractImplementations;

import CNFSATSolver.CDCLSolver.CoreClasses.*;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;
import CNFSATSolver.CoreComponents.Pair;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractClause implements Clause {

    protected ArrayList<Literal> clause = new ArrayList<>();
    protected int numVariables;
    protected int clauseNumber = -1;
    protected boolean wasLearnedClause = false;
    protected double activity = 0;
    protected boolean isLocked = false;

    public AbstractClause(int numVariables) {
        this.numVariables = numVariables;
    }

    public void setClauseList(ArrayList<Literal> clause) {
        this.clause = clause;
    }

    public void setClauseNumber(int num) {
        this.clauseNumber = num;
    }

    public double getActivity() {
        return activity;
    }

    public int getClauseNumber() {
        return clauseNumber;
    }

    public void lockClause() {
        isLocked = true;
    }

    public void unlockClause() {
        isLocked = false;
    }

    public void resolve(Variable variable) {
        Clause antecedent = variable.getSingleVariableAssignment().getAntecedent();
        ArrayList<Literal> list = antecedent.getClause();
        boolean seenVariable = false;
        Iterator<Literal> iter = clause.iterator();
        while((!seenVariable) && (iter.hasNext())) {
            Literal lit = iter.next();
            if (lit.getVariable().equals(variable)) {
                iter.remove();
                seenVariable = true;
            }
        }

        for (Literal lit : list) {
            if (!lit.getVariable().equals(variable)) {
                if (!clause.contains(lit)) {
                    clause.add(lit);
                }
            }
        }
    }

    public int getNumVariablesAtDecisionLevel(int decisionLevel) {
        int ret = 0;
        for (Literal lit: clause) {
            if (lit.getVariable().getSingleVariableAssignment().getDecisionLevel() == decisionLevel) {
                ret++;
            }
        }
        return ret;
    }

    public void addLiteral(Literal literal) {
        clause.add(literal);
    }

    public abstract void initializeClause();

    public abstract void removeClause();

    public void decayActivity(double decayFactor) {
        if (wasLearnedClause) {
            activity *= decayFactor;
        }
    }

    public void bumpActivity(double bumpActivity) {
        if (wasLearnedClause) {
            activity += bumpActivity;
        }
    }

    public void setActivity(double activity) {
        this.activity = activity;
    }

    public ArrayList<Literal> getClause() {
        return clause;
    }

    public int getNumVariables() {
        return numVariables;
    }

    public abstract Pair<Integer, Literal> getClauseStatus();

    public abstract void wasLearnedClause(int learnedClauseIndexSecondHighestDecisionLevel);

    public abstract Clause copy();

    public boolean containsVariable(Variable variable) {
        for (Literal lit : clause) {
            if (lit.getVariable().equals(variable)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLearnedClause() {
        return wasLearnedClause;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public int getSize() {
        return clause.size();
    }

    public void variableAssignedSatisfying(SingleVariableAssignment assignment) {
    }

    public void variableAssignedNotSatisfying(SingleVariableAssignment assignment) {
    }

    public void variableUnassignedSatisfying(SingleVariableAssignment assignment) {
    }

    public void variableUnassignedNotSatisfying(SingleVariableAssignment assignment) {
    }

    @Override
    public String toString() {
        return clause.toString();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(clauseNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Clause)) {
            return false;
        }

        Clause clauseOther = (Clause) obj;
        return clauseOther.getClauseNumber() == clauseNumber;
    }

    @Override
    public int getNumLiterals() {
        return clause.size();
    }

    @Override
    public Literal getLitNum(int index) {
        return clause.get(index);
    }
}
