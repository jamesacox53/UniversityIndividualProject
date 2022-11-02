package CNFSATSolver.CDCLSolver.CoreClasses.AbstractImplementations;

import CNFSATSolver.CDCLSolver.CoreClasses.*;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;
import CNFSATSolver.CoreComponents.CnfFileParser;
import CNFSATSolver.CoreComponents.Interfaces.SatProblem;


public abstract class CDCLCnfFileParser extends CnfFileParser<Variable, Literal, Clause, CnfFormula, VariablesAssignments> {
    @Override
    protected VariablesAssignments createVariablesAssignments(Variable[] variables, Literal[] positiveLiterals, Literal[] negatedLiterals, int numVariables) {
        return new VariablesAssignments(variables, positiveLiterals, negatedLiterals, numVariables);
    }

    @Override
    protected CnfFormula createCnfFormula(int numVariables, int numClauses) {
        return new CnfFormula(numVariables, numClauses);
    }

    @Override
    protected abstract Clause createClause(int numVariables);

    @Override
    protected abstract Variable createVariable(int variableNum);

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
        cnfFormula.addProblemConstraintClause(clause);
    }

    @Override
    protected void addLiteralToClause(Clause clause, Literal literal) {
        clause.addLiteral(literal);
    }

    @Override
    protected SatProblem createSatProblem(CnfFormula cnfFormula, VariablesAssignments variablesAssignments) {
        return new SatProblemForCDCL(cnfFormula, variablesAssignments);
    }

}
