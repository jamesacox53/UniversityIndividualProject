package CNFSATSolver.DPLLSolver;

import CNFSATSolver.CoreComponents.Interfaces.ClauseI;
import CNFSATSolver.CoreComponents.Pair;
import CNFSATSolver.CoreComponents.Interfaces.SatProblem;

public class SatProblemForDPLL extends Pair<CnfFormula, VariablesAssignments> implements SatProblem {

    SatProblemForDPLL(CnfFormula formula, VariablesAssignments assignments) {
        super(formula, assignments);
    }

    @Override
    public int getNumProblemClauses() {
        return left.getSizeFormula();
    }

    @Override
    public ClauseI getClauseNum(int index) {
        return left.getClause(index);
    }

    @Override
    public boolean getSolveable() {
        return right.getSolveable();
    }

}
