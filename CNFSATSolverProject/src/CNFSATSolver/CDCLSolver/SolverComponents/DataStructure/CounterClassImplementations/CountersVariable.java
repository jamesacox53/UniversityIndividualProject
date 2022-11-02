package CNFSATSolver.CDCLSolver.SolverComponents.DataStructure.CounterClassImplementations;

import CNFSATSolver.CDCLSolver.CoreClasses.AbstractImplementations.AbstractVariable;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;

public class CountersVariable extends AbstractVariable implements Variable {

    public CountersVariable(int variable) {
        super(variable);
    }

    protected SingleVariableAssignment newSingleVariableAssignment() {
        return new CountersSingleVariableAssignment(this);
    }
}
