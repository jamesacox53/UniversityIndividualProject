package CNFSATSolver.CDCLSolver.SolverComponents.ClauseDeletion;

import CNFSATSolver.CDCLSolver.CoreClasses.Interfaces.*;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * ActivityDeletion deletes learnt clauses that it believes to be unimportant. ActivityDeletion is based on the code in
 * the MiniSat Solver v2.2.0 (minisat.se/downloads/minisat-2.2.0.tar.gz) and the MiniSat paper
 * “An Extensible SAT-solver [extended version 1.2]” (http://minisat.se/downloads/MiniSat.pdf) by Dr Niklas Sorensson
 * and Dr Niklas Een. The Activity Based Clause Deletion heuristic was fully understood by looking at the source code of
 * MiniSat v2.2.0 and the implementation of ActivityDeletion is adapted from this source code so full credit to
 * MiniSat v2.2.0 and Dr Niklas Sorensson and Dr Niklas Een for the implementation of this heuristic. The code wasn't
 * copied and pasted but was adapted to be used in my SAT solver as a class that implements the
 * ClauseDeletion interface. The code was also adapted from C++ to Java.
 */

public class ActivityDeletion implements ClauseDeletion {
    private List<Clause> learntClauses;
    private double clauseIncrement = 1;
    private double clauseDecay;
    private double initialMaxNumLearntClauses;
    private double geometricCommonRatio;
    private double maxNumLearntClauses;
    private Comparator<Clause> comparator;
    private double activityLimit = Math.pow(2, 26);

    public ActivityDeletion(List<Clause> learntClauses, double clauseDecay, double initialMaxNumLearntClauses, double geometricCommonRatio) {

        this.learntClauses = learntClauses;
        this.clauseDecay = (1 / clauseDecay);
        this.initialMaxNumLearntClauses = initialMaxNumLearntClauses;
        this.geometricCommonRatio = geometricCommonRatio;
        this.maxNumLearntClauses = initialMaxNumLearntClauses;

        comparator = new Comparator<Clause>() {
            @Override
            public int compare(Clause c1, Clause c2) {
                double activity1 = c1.getActivity();
                double activity2 = c2.getActivity();

                if (activity1 < activity2) {
                    return -1;
                } else if (activity1 > activity2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
    }

    public void decayClauseActivities() {
        clauseIncrement *= clauseDecay;
    }

    public void bumpClauseActivity(Clause clause) {
       clause.bumpActivity(clauseIncrement);

       if (clause.getActivity() >= activityLimit) {

           double decayFactor = Math.pow(2, -26);
            for (Clause learnedClause : learntClauses) {
                learnedClause.decayActivity(decayFactor);
            }

           clauseIncrement *= decayFactor;
       }
    }

    public void deleteClauses() {
        int InitialnumLearntClauses = learntClauses.size();
        if (InitialnumLearntClauses >= maxNumLearntClauses) {
            maxNumLearntClauses *= geometricCommonRatio;

            learntClauses.sort(comparator);

            double limit = clauseIncrement / InitialnumLearntClauses;

            deleteHalfway(InitialnumLearntClauses);

            deleteActivityLessThanLimit(limit);
        }
    }


    private void deleteHalfway(int InitialnumLearntClauses) {

        boolean seenHalfway = false;
        int numClausesSeen = 0;

        Iterator<Clause> clauseIterator = learntClauses.iterator();

        while (!seenHalfway && clauseIterator.hasNext()) {
            Clause clause = clauseIterator.next();

            numClausesSeen++;

            if (numClausesSeen >= InitialnumLearntClauses / 2.0) {
                seenHalfway = true;

            } else if (clause.getSize() > 2 && !clause.isLocked()) {

                clause.removeClause();
                clauseIterator.remove();
            }
        }
    }

    private void deleteActivityLessThanLimit(double limit) {

        boolean seenLimit = false;

        Iterator<Clause> clauseIterator = learntClauses.iterator();

        while (!seenLimit && clauseIterator.hasNext()) {
            Clause clause = clauseIterator.next();

            if (clause.getActivity() >= limit) {
                seenLimit = true;

            } else if (clause.getSize() > 2 && !clause.isLocked()) {

                clause.removeClause();
                clauseIterator.remove();
            }
        }
    }
}
