package CNFSATSolver.CoreComponents.Interfaces;

public interface CnfFileParserI {

    public SatProblem parseFileToCreateFormula(String inputFilePath);

}
