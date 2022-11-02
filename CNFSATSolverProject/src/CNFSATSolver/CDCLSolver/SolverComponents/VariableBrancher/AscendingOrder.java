package CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher;

import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;
import CNFSATSolver.CDCLSolver.CoreClasses.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AscendingOrder extends MinHeap<Variable> implements VariablePicker {

    private Variable[] variables;
    private Literal[] positiveLiterals;
    private Literal[] negatedLiterals;
    private int[] variablesLocations;
    private int[] positiveLiteralsLocations;
    private int[] negatedLiteralsLocations;

    public AscendingOrder(Comparator<Variable> comparator, Variable[] variables, Literal[] positiveLiterals, Literal[] negatedLiterals) {
        super(comparator);
        this.variables = variables;
        this.positiveLiterals = positiveLiterals;
        this.negatedLiterals = negatedLiterals;

        positiveLiteralsLocations = new int[positiveLiterals.length];
        negatedLiteralsLocations = new int[negatedLiterals.length];
        variablesLocations = new int[variables.length];

        Arrays.fill(positiveLiteralsLocations, -1);
        Arrays.fill(negatedLiteralsLocations, -1);
        Arrays.fill(variablesLocations, -1);
    }

    @Override
    public Literal getNextLiteral() {
        Variable var = super.removeMin();
        while (var != null && var.getSingleVariableAssignment().isAssigned()) {
            var = super.removeMin();
        }

        if (var == null) {
            return null;
        }

        return negatedLiterals[var.getVariable() - 1];
    }

    @Override
    protected void setLocation(Variable elem, int loc) {
        int variable = elem.getVariable();
        variablesLocations[variable - 1] = loc;
    }

    @Override
    protected int getLocation(Variable elem) {

        return variablesLocations[elem.getVariable() - 1];
    }

    public void initialise(CnfFormula formula) {

        for (Variable var : variables) {
            super.add(var);
        }
    }

    public void addVariableLiterals(Variable variable) {
        if (!contains(variable)) {
            super.add(variable);
        }
    }

    @Override
    public void removeVariable(Variable variable) {

        int var = variable.getVariable();
        int location = variablesLocations[var - 1];

        if (location != -1) {
            super.removeIndex(location);
        }
    }

    public boolean contains(Variable var) {
        int loc = variablesLocations[var.getVariable() - 1];

        if (loc == -1) {
            return false;
        }

        return true;
    }

    public void decayLiterals(int numConflicts) {
    }

    public void bumpActivities(List<Literal> lits) {
    }

    public boolean contains(Literal elem) {
        return false;
    }
}
