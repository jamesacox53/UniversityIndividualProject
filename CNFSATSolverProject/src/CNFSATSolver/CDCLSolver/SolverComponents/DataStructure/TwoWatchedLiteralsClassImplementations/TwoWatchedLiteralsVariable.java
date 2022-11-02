package CNFSATSolver.CDCLSolver.SolverComponents.DataStructure.TwoWatchedLiteralsClassImplementations;

import CNFSATSolver.CDCLSolver.CoreClasses.AbstractImplementations.AbstractVariable;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;

public class TwoWatchedLiteralsVariable extends AbstractVariable implements Variable {

    public TwoWatchedLiteralsVariable(int variable) {
        super(variable);
    }

    protected SingleVariableAssignment newSingleVariableAssignment() {
        return new TwoWatchedLiteralsSingleVariableAssignment(this);
    }
}
