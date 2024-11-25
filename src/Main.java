import com.jake.Circle;
import com.jake.Dot;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Main {

    /**
     * Number of moves made in the current solution attempt.
     */
    static int solutionMoves = 0;

    /**
     * Total number of moves made to track program runtime.
     */
    static long totalMoves = 0;

    /**
     * The upper bound on moves made in the current solution attempt.
     */
    static final int MAX_SOLUTION_MOVES = 80;

    /**
     * The absolute upper bound on the times going through the loop.
     */
    static final long MAX_TOTAL_MOVES = 10000000;

    /**
     * Define the success state: the puzzle is solved when all Circle values match this pattern.
     */
    static final String successState = "GGGRYGGG YGRYYGGY GYYGGYYR GGGGGYRG";

    /**
     * The success state represented as a character array.
     */
    static final char[] successArray = successState.toCharArray();

    /**
     * The total list of states that have already been attained. This will be used as memoization,
     *   keeping track of arrangements we've already tried and essentially making sure we try unique
     *   moves instead of rotating everything about.
     */
    static ArrayList<String> exploredStates = new ArrayList<>();

    /**
     * A parallel array of scores corresponding to each explored state.
     */
    static ArrayList<Double> exploredStateScores = new ArrayList<>();

    /**
     * The list of moves that leads from the start state to the end state.
     */
    static ArrayDeque<String> solution = new ArrayDeque<>();

    /**
     * Current state of all Circle values.
     */
    static String currentState = "";

    /**
     * The score value of the current state.
     */
    static double currentScore = 0.0;

    /**
     * The highest score value attained so far, used to track progress towards the goal.
     */
    static double highestScore = 0.0;

    /**
     * A tolerance value for future scores. This will be subtracted from the highest score to
     *  allow for the possibility of a solution that needs to "get worse before it gets better."
     */
    final static double TOLERANCE = 0.1;

    /**
     * Whether the puzzle has been solved.
     */
    static boolean solved = false;

    public static void main(String[] args) {

        // Initialize the Dots. For my personal convenience, Dot variable names will consist
        //  of the Dot's row, then its column.
        Dot dot11 = new Dot('R'),
                dot12 = new Dot('G'),
                dot13 = new Dot('Y'),
                dot14 = new Dot('G');
        Dot dot21 = new Dot('Y'),
                dot22 = new Dot ('G'),
                dot23 = new Dot('G'),
                dot24 = new Dot('Y'),
                dot25 = new Dot('G');
        Dot dot31 = new Dot('Y'),
                dot32 = new Dot('G'),
                dot33 = new Dot('Y'),
                dot34 = new Dot('G'),
                dot35 = new Dot('G'),
                dot36 = new Dot('Y'),
                dot37 = new Dot('R');
        Dot dot41 = new Dot('G'),
                dot42 = new Dot('Y'),
                dot43 = new Dot('G'),
                dot44 = new Dot('R');
        Dot dot51 = new Dot('G'),
                dot52 = new Dot('G'),
                dot53 = new Dot('G');


        // Initialize the Circles.
        Circle upperLeftCircle = new Circle("upper-left", new Dot[]{dot11, dot12, dot23, dot34, dot42, dot41, dot31, dot21});
        Circle lowerLeftCircle = new Circle("lower-left", new Dot[]{dot22, dot23, dot35, dot43, dot52, dot51, dot41, dot32});
        Circle lowerRightCircle = new Circle("lower-right", new Dot[]{dot23, dot24, dot36, dot44, dot53, dot52, dot42, dot33});
        Circle upperRightCircle = new Circle("upper-right", new Dot[]{dot13, dot14, dot25, dot37, dot44, dot43, dot34, dot23});
        Circle[] circleList = new Circle[]{upperLeftCircle, lowerLeftCircle, lowerRightCircle, upperRightCircle};

        // Try to solve the puzzle! Best of luck, computer, you can do it! :D
        System.out.println("Attempting to solve puzzle with max solution moves of " + MAX_SOLUTION_MOVES
            + " and maximum total moves of " + MAX_TOTAL_MOVES + ".\n");
        solved = solve(circleList);
        if (!solved) {
            System.out.println("No solution found...");
            System.out.println("Total moves: " + totalMoves);
            System.out.println("Explored solution size: " + exploredStates.size());
        } else {
            System.out.println("***Solution found!!!!*** Here's how it's done: \n");
            for (String s : solution) {
                System.out.println(s);
            }
        }
    }

    /**
     * Attempt to solve the puzzle!!!
     * @param circleList The Circles used in this puzzle.
     * @return True if the puzzle was successfully solved, false if not.
     */
    private static boolean solve(Circle[] circleList) {
        if (currentState.compareTo(successState) == 0) {
            return true;
        } else if (solutionMoves > MAX_SOLUTION_MOVES || totalMoves > MAX_TOTAL_MOVES) {
            return false;
        } else if (exploredStates.contains(currentState)) {
            return false;
        } else {
            exploredStates.add(currentState);
            exploredStateScores.add(currentScore);
        }

        // Print out diagnostic data at regular intervals.
        if (totalMoves % 1000 == 0) {
            System.out.println("totalMoves: " + totalMoves + ", Current state: " + currentState
                    + ", Current state score: " + computeScore(currentState) + ", Explored state size: "
                    + exploredStates.size());
        }

        // Exit this state early if its score is below the allowable tolerance.
        if (currentScore < (highestScore - TOLERANCE)) {
            return false;
        }

        // Try to rotate each circle clockwise and see if it solves the puzzle.
        for (Circle c : circleList) {
            c.rotateRight();
            currentState = currentState(circleList);
            currentScore = computeScore(currentState);
            solutionMoves++;
            totalMoves++;
            solved = solve(circleList);
            if (solved) {
                solution.push("Rotate " + c.getId() + " circle clockwise.");
                return true;
            } else {
                c.rotateLeft();
                currentState = currentState(circleList);
                currentScore = computeScore(currentState);
                solutionMoves--;
            }
        }

        // Try to rotate each circle counter-clockwise and see if it solves the puzzle.
        for (Circle c: circleList) {
            c.rotateLeft();
            currentState = currentState(circleList);
            currentScore = computeScore(currentState);
            solutionMoves++;
            totalMoves++;
            solved = solve(circleList);
            if (solved) {
                solution.push("Rotate " + c.getId() + " circle counter-clockwise.");
                return true;
            } else {
                c.rotateRight();
                currentState = currentState(circleList);
                currentScore = computeScore(currentState);
                solutionMoves--;
            }
        }

        // At this point, all possible moves have been exhausted and no solution has been found.
        return false;
    }

    /**
     * Return the current state of the puzzle, represented as a String of Circle colors.
     *  This prints all eight values of each Circle, so the same Dot will appear more than once.
     * @param circles The array of Circles to consider.
     * @return The current state of the puzzle as a String of Circle colors.
     */
    private static String currentState(Circle[] circles) {
        StringBuilder result = new StringBuilder();
        for (Circle circle : circles) {
            result.append(" ").append(circle.toString());
        }
        return result.toString().trim();
    }

    /**
     * Compute the "score" of the current state as a measure of how close it is to the solution.
     * @param stateString The current state represented as a String of Dot colors.
     * @return The score of the current state. 0 is a complete mismatch, 1 is the solution state.
     */
    private static double computeScore(String stateString) {
        double matches = 0, mismatches = 0;
        char[] stateArray = stateString.toCharArray();
        for (int i = 0; i < stateArray.length; i++) {
            if (stateArray[i] == successArray[i]) {
                matches++;
            } else {
                mismatches++;
            }
        }
        return matches / (matches + mismatches);
    }
}