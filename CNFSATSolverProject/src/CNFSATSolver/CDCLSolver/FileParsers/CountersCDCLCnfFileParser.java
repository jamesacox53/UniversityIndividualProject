package CNFSATSolver.CDCLSolver.FileParsers;

import CNFSATSolver.CDCLSolver.CoreClasses.AbstractImplementations.CDCLCnfFileParser;
import CNFSATSolver.CDCLSolver.SolverComponents.DataStructure.CounterClassImplementations.*;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;

public class CountersCDCLCnfFileParser extends CDCLCnfFileParser {

    @Override
    protected Clause createClause(int numVariables) {
        return new CountersClause(numVariables);
    }

    @Override
    protected Variable createVariable(int variableNum) {
        return new CountersVariable(variableNum);
    }

}
