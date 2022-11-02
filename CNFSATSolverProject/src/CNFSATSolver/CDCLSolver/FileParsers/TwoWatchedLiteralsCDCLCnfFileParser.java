package CNFSATSolver.CDCLSolver.FileParsers;

import CNFSATSolver.CDCLSolver.CoreClasses.AbstractImplementations.CDCLCnfFileParser;
import CNFSATSolver.CDCLSolver.SolverComponents.DataStructure.TwoWatchedLiteralsClassImplementations.*;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;

public class TwoWatchedLiteralsCDCLCnfFileParser extends CDCLCnfFileParser {

    @Override
    protected Clause createClause(int numVariables) {
        return new TwoWatchedLiteralsClause(numVariables);
    }

    @Override
    protected Variable createVariable(int variableNum) {
        return new TwoWatchedLiteralsVariable(variableNum);
    }

}
