package CNFSATSolver.CDCLSolver.CoreClasses;

import CNFSATSolver.CoreComponents.Interfaces.ClauseI;
import CNFSATSolver.CoreComponents.Pair;
import CNFSATSolver.CoreComponents.Interfaces.SatProblem;

public class SatProblemForCDCL extends Pair<CnfFormula, VariablesAssignments> implements SatProblem {

    public SatProblemForCDCL(CnfFormula formula, VariablesAssignments assignments) {
        super(formula, assignments);
    }

    @Override
    public int getNumProblemClauses() {
        return left.getNumConstraintClauses();
    }

    @Override
    public ClauseI getClauseNum(int index) {
        return left.getConstraintClauseNum(index);
    }

    @Override
    public boolean getSolveable() {
        return right.getSolveable();
    }
}
