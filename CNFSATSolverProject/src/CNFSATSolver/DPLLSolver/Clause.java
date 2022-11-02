package CNFSATSolver.DPLLSolver;

import CNFSATSolver.CoreComponents.Interfaces.ClauseI;
import java.util.ArrayList;
import java.util.List;

public class Clause implements ClauseI {

    private final List<Literal> clause = new ArrayList<>();
    private final int numVariables;
    private int numLiteralsCurrSatisfyClauseCounter = 0;
    private int numLiteralsCurrDontSatisfyClauseCounter = 0;

    public Clause(int numVariables) {
        this.numVariables = numVariables;
    }

    public void addLiteral(Literal literal) {
        clause.add(literal);
        SingleVariableAssignment assignment = literal.getVariable().getSingleVariableAssignment();
        boolean isNegated = literal.isNegated();
        if(isNegated) {
            assignment.addClauseVariableAppearsNegativelyIn(this);
        } else {
            assignment.addClauseVariableAppearsPositivelyIn(this);
        }
    }

    public List<Literal> getClause() {
        return clause;
    }

    public void incrementNumLiteralsSatisfy() {
        numLiteralsCurrSatisfyClauseCounter++;
    }

    public void decrementNumLiteralsSatisfy() {
        numLiteralsCurrSatisfyClauseCounter--;
    }

    public void incrementNumLiteralsDontSatisfy() {
        numLiteralsCurrDontSatisfyClauseCounter++;
    }

    public void decrementNumLiteralsDontSatisfy() {
        numLiteralsCurrDontSatisfyClauseCounter--;
    }

    public int getNumLiterals() {
        return clause.size();
    }

    public Literal getLitNum(int index) {
        return clause.get(index);
    }

    public int getNumVariables() {
        return numVariables;
    }

    public boolean isSatisfied() {
        return numLiteralsCurrSatisfyClauseCounter > 0;
    }

    public boolean isUnSatisfied() {
        return numLiteralsCurrDontSatisfyClauseCounter == clause.size();
    }

    public boolean isUnitClause() {
        return (!isSatisfied()) && (numLiteralsCurrDontSatisfyClauseCounter == (clause.size() - 1));
    }
}
