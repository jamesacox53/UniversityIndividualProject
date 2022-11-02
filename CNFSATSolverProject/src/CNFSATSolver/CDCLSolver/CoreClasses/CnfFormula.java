package CNFSATSolver.CDCLSolver.CoreClasses;

import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;

import java.util.LinkedList;
import java.util.List;

public class CnfFormula {

    private final List<Clause> problemConstraintClauses = new LinkedList<>();
    private final List<Clause> learntClauses = new LinkedList<>();
    private final int numVariables;
    private int numClauses;
    private int clauseNumber = 0;

    public CnfFormula(int numVariables, int numClauses) {
        this.numVariables = numVariables;
        this.numClauses = numClauses;
    }

    public void addProblemConstraintClause(Clause clause) {
        problemConstraintClauses.add(clause);
        clause.setClauseNumber(clauseNumber);
        clause.initializeClause();
        clauseNumber++;
    }

    public void addLearntClause(Clause clause) {
        learntClauses.add(clause);
        clause.setClauseNumber(clauseNumber);
        clause.initializeClause();
        clauseNumber++;
    }


    public void incrementNumClauses() {
        numClauses++;
    }

    public Clause getConstraintClauseNum(int index) {
        return problemConstraintClauses.get(index);
    }

    public List<Clause> getProblemConstraintClauses() {
        return problemConstraintClauses;
    }

    public List<Clause> getLearntClauses() {
        return learntClauses;
    }

    public int getNumConstraintClauses() {
        return problemConstraintClauses.size();
    }

    public int getNumLearntClauses() {
        return learntClauses.size();
    }

    public int getNumVariables() {
        return numVariables;
    }

    public int getNumClauses() {
        return numClauses;
    }

}
