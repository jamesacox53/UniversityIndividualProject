package CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher;

import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;
import CNFSATSolver.CDCLSolver.CoreClasses.*;

import java.util.*;

public class VSIDSVariablePicker extends MinHeap<Literal> implements VariablePicker {

    private Literal[] positiveLiterals;
    private Literal[] negatedLiterals;
    private double decayFactor;
    private double bumpActivity;
    private int decayFrequency;
    private int decayThreshold;
    private int[] positiveLiteralsLocations;
    private int[] negatedLiteralsLocations;

    public VSIDSVariablePicker(Comparator<Literal> comparator, Literal[] positiveLiterals, Literal[] negatedLiterals, double decayFactor, double bumpActivity, int decayFrequency) {
        super(comparator);

        this.positiveLiterals = positiveLiterals;
        this.negatedLiterals = negatedLiterals;
        this.decayFactor = decayFactor;
        this.bumpActivity = bumpActivity;
        this.decayFrequency = decayFrequency;
        this.decayThreshold = decayFrequency;

        positiveLiteralsLocations = new int[positiveLiterals.length];
        negatedLiteralsLocations = new int[negatedLiterals.length];

        Arrays.fill(positiveLiteralsLocations, -1);
        Arrays.fill(negatedLiteralsLocations, -1);

    }

    public Literal getNextLiteral() {
        Literal lit = super.removeMin();
        while (lit != null && lit.getVariable().getSingleVariableAssignment().isAssigned()) {
            lit = super.removeMin();
        }
        return lit;
    }

    public void initialise(CnfFormula formula) {

        List<Clause> problemConstraintClauses = formula.getProblemConstraintClauses();
        List<Clause> learntClauses = formula.getLearntClauses();

        for (Clause clause : problemConstraintClauses) {
            List<Literal> lits = clause.getClause();
            bumpActivities(lits);
        }

        for (Clause clause : learntClauses) {
            List<Literal> lits = clause.getClause();
            bumpActivities(lits);
        }

        for (Literal lit : positiveLiterals) {
            super.add(lit);
        }

        for (Literal lit : negatedLiterals) {
            super.add(lit);
        }
    }

    public void addVariableLiterals(Variable variable) {

        int var = variable.getVariable();

        Literal positiveLit = positiveLiterals[var - 1];
        Literal negatedLit = negatedLiterals[var - 1];

        if (!contains(positiveLit)) {
            super.add(positiveLit);
        }

        if (!contains(negatedLit)) {
            super.add(negatedLit);
        }
    }

    public void bumpActivities(List<Literal> lits) {

        for (Literal lit : lits) {
            lit.bumpActivity(bumpActivity);
            if (contains(lit)) {
                int loc = getLocation(lit);
                super.siftUp(loc, lit);
            }
        }
    }

    public void decayLiterals(int numConflicts) {

        if (numConflicts >= decayThreshold) {
            decayThreshold += decayFrequency;

            for (Literal lit : positiveLiterals) {
                lit.decayActivity(decayFactor);
            }

            for (Literal lit : negatedLiterals) {
                lit.decayActivity(decayFactor);
            }
        }
    }

    @Override
    protected int getLocation(Literal elem) {
        boolean isNegated = elem.isNegated();
        int variable = elem.getVariable().getVariable();
        if (isNegated) {
            return negatedLiteralsLocations[variable - 1];
        } else {
            return positiveLiteralsLocations[variable - 1];
        }
    }

    @Override
    protected void setLocation(Literal elem, int loc) {
        boolean isNegated = elem.isNegated();
        int variable = elem.getVariable().getVariable();
        if (isNegated) {
            negatedLiteralsLocations[variable - 1] = loc;
        } else {
            positiveLiteralsLocations[variable - 1] = loc;
        }
    }

    public boolean contains(Literal elem) {
        boolean isNegated = elem.isNegated();
        int variable = elem.getVariable().getVariable();
        if (isNegated) {
            int loc = negatedLiteralsLocations[variable - 1];
            if (loc != -1) {
                return true;
            } else {
                return false;
            }
        } else {
            int loc = positiveLiteralsLocations[variable - 1];
            if (loc != -1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void removeVariable(Variable variable) {
        int var = variable.getVariable();
        int locPositive = positiveLiteralsLocations[var - 1];

        if (locPositive != -1) {
            super.removeIndex(locPositive);
        }

        int locNegative = negatedLiteralsLocations[var - 1];

        if (locNegative != -1) {
            super.removeIndex(locNegative);
        }
    }

    public boolean contains(Variable var) {
        return false;
    }

}
