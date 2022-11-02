package CNFSATSolver.Tests;

import CNFSATSolver.CDCLSolver.CoreClasses.Literal;
import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;
import CNFSATSolver.CDCLSolver.SolverComponents.DataStructure.TwoWatchedLiteralsClassImplementations.*;
import CNFSATSolver.CDCLSolver.SolverComponents.VariableBrancher.VSIDSVariablePicker;

import java.util.ArrayList;
import java.util.Comparator;

public class TestVSIDSDecayLiterals {

    public static void main(String[] args) {

        Variable[] variables = createVariables(4);
        Literal[] positiveLiterals = createPositiveLiterals(variables);
        Literal[] negatedLiterals = createNegatedLiterals(variables);

        Comparator<Literal> VSIDSMaxActivityComparator = new Comparator<Literal>() {
            @Override
            public int compare(Literal o1, Literal o2) {
                double activity1 = o1.getActivity();
                double activity2 = o2.getActivity();
                if (activity1 > activity2) {
                    return -1;
                } else if (activity1 < activity2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };


        VSIDSVariablePicker picker = new VSIDSVariablePicker(VSIDSMaxActivityComparator, positiveLiterals,
                negatedLiterals, 0.5, 6, 20);

        for (int i = 1; i <= positiveLiterals.length; i++) {
            positiveLiterals[i - 1].setActivity(i);
        }

        for (int i = 1; i <= negatedLiterals.length; i++) {
            negatedLiterals[i - 1].setActivity(4 + i);
        }


        for (int i = 0; i < variables.length; i++) {
            picker.addVariableLiterals(variables[i]);
        }

        ArrayList<Literal> lits = new ArrayList<>();
        lits.add(positiveLiterals[0]);
        lits.add(negatedLiterals[1]);

        picker.bumpActivities(lits);

        picker.removeVariable(variables[2]);

        picker.decayLiterals(25);

        lits = new ArrayList<>();
        lits.add(positiveLiterals[2]);

        picker.bumpActivities(lits);

        picker.add(positiveLiterals[2]);

    }

    public static Variable[] createVariables(int numVariables) {
        Variable[] variables = new Variable[numVariables];
        for (int i = 0; i < variables.length; i++) {
            variables[i] = new TwoWatchedLiteralsVariable(i + 1);
        }
        return variables;
    }

    public static Literal[] createPositiveLiterals(Variable[] variables) {
        Literal[] positiveLiterals = new Literal[variables.length];
        for (int i = 0; i < variables.length; i++) {
            positiveLiterals[i] = new Literal(variables[i], false);
        }
        return positiveLiterals;
    }

    public static Literal[] createNegatedLiterals(Variable[] variables) {
        Literal[] negatedLiterals = new Literal[variables.length];
        for (int i = 0; i < variables.length; i++) {
            negatedLiterals[i] = new Literal(variables[i], true);
        }
        return negatedLiterals;
    }
}
