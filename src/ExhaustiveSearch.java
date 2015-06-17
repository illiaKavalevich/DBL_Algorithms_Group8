
import java.util.*;

public class ExhaustiveSearch extends Algorithm {

    HashMap<Integer, Set<Label>> solutions = new HashMap<>();
    Set<Label> bestSolution;
    int bestScore;
    int score;
    boolean stop;

    public ExhaustiveSearch() {

    }

    @Override
    public void determineLabels() {
        bestSolution = new HashSet<Label>();
        bestScore = calcScore(bestSolution);
        createSolutions(0, bestSolution);
        selectBestResult();
        removeOverlap();
    }

    public void createSolutions(int n, Set<Label> sol) {
        if (!stop) {
            if (n > points.size() - 1) {
                score = calcScore(sol);

                if (score < bestScore) {
                    bestScore = score;
                    bestSolution = new HashSet<Label>(sol);

                }
            } else {
                for (Label label : points.get(n).possibleLabels) {
                    if (!stop) {
                        //System.out.println("test1" + stop);
                        sol.add(label);
                        createSolutions(n + 1, sol);
                        sol.remove(label);
                    } else {
                        System.out.println("test");
                        break;
                    }

                }
            }

        } else {
            System.out.println("stopping");

        }

    }

    public int calcScore(Set<Label> sol) {
        int score = 0;
        if (sol.isEmpty()) {
            return Integer.MAX_VALUE;
        } else {
            for (Label label : sol) {
                label.active = true;
            }
            for (Label label : sol) {
                score = score + cD.getActDegree(label);
            }
            for (Label label : sol) {
                label.active = false;
            }
            //System.out.println(sol);
            //System.out.println("score: " + score);
            return score;
        }
    }

    @Override
    public void stopRunning() {
        stop = true;
    }

    public void selectBestResult() {
        for (Label label : bestSolution) {
            label.active = true;
            numLabels++;
        }
    }

}
