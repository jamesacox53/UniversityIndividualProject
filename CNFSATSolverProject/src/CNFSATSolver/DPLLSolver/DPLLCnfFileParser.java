package CNFSATSolver.DPLLSolver;

import CNFSATSolver.CoreComponents.CnfFileParser;
import CNFSATSolver.CoreComponents.Interfaces.SatProblem;


public class DPLLCnfFileParser extends CnfFileParser<Variable, Literal, Clause, CnfFormula, VariablesAssignments> {
    @Override
    protected VariablesAssignments createVariablesAssignments(Variable[] variables, Literal[] positiveLiterals, Literal[] negatedLiterals, int numVariables) {
        return new VariablesAssignments(variables, numVariables);
    }

    @Override
    protected CnfFormula createCnfFormula(int numVariables, int numClauses) {
        return new CnfFormula(numVariables, numClauses);
    }

    @Override
    protected Clause createClause(int numVariables) {
        return new Clause(numVariables);
    }

    @Override
    protected Variable createVariable(int variableNum) {
        return new Variable(variableNum);
    }

    @Override
    protected Literal createLiteral(Variable variable, boolean isNegated) {
        return new Literal(variable, isNegated);
    }

    @Override
    protected Variable[] createArrayVariables(int numVariables) {
        return new Variable[numVariables];
    }

    @Override
    protected Literal[] createArrayLiterals(int numVariables) {
        return new Literal[numVariables];
    }

    @Override
    protected void addProblemConstraintClause(CnfFormula cnfFormula, Clause clause) {
        cnfFormula.addClause(clause);
    }

    @Override
    protected void addLiteralToClause(Clause clause, Literal literal) {
        clause.addLiteral(literal);
    }

    @Override
    protected SatProblem createSatProblem(CnfFormula cnfFormula, VariablesAssignments variablesAssignments) {
        return new SatProblemForDPLL(cnfFormula, variablesAssignments);
    }
}
