package CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher;

import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;
import CNFSATSolver.CDCLSolver.CoreClasses.*;

import java.util.List;

public interface VariablePicker {

    public Literal getNextLiteral();

    public void bumpActivities(List<Literal> lits);

    public void initialise(CnfFormula formula);

    public void addVariableLiterals(Variable variable);

    public void removeVariable(Variable variable);

    public void decayLiterals(int numConflicts);

    public boolean contains(Literal elem);

    public boolean contains(Variable elem);
}
