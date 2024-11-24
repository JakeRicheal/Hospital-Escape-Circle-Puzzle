import com.jake.Circle;
import com.jake.Dot;

import java.util.ArrayDeque;

public class Main {

    // Number of moves made in the current solution attempt.
    static int solutionMoves = 0;

    // Total number of moves made to track program runtime.
    static int totalMoves = 0;

    // The upper bound on moves made in the current solution attempt.
    static final int MAX_SOLUTION_MOVES = 100;

    // The absolute upper bound on the times going through the loop.
    static final int MAX_TOTAL_MOVES = 5000;

    // Define the success state: the puzzle is solved when all Circle values match this pattern.
    static final String successState = "GGGRYGGG YGRYYGGY GYYGGYYR GGGGGYRG";

    // The total list of states that have already been attained. This will be used as memoization,
    //  keeping track of arrangements we've already tried and essentially making sure we try unique
    //  moves instead of rotating everything about.
    static ArrayDeque<String> exploredStates = new ArrayDeque<>();

    // The list of moves that leads from the start state to the end state.
    static ArrayDeque<String> solution = new ArrayDeque<>();

    // Current state of all Circle values;
    static String currentState;

    // Whether the puzzle has been solved.
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
        solve(circleList);
        if (!solved) {
            System.out.println("No solution found...");
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
        } else {
            currentState = currentState(circleList);
            if (exploredStates.contains(currentState)) {
                return false;
            } else {
                exploredStates.add(currentState);
            }
        }

        // Try to rotate each circle clockwise and see if it solves the puzzle.
        for (Circle c : circleList) {
            c.rotateRight();
            solutionMoves++;
            totalMoves++;
            solved = solve(circleList);
            if (solved) {
                solution.push("Rotate " + c.getId() + " circle clockwise.");
                return true;
            } else {
                c.rotateLeft();
                solutionMoves--;
            }
        }

        // Try to rotate each circle counter-clockwise and see if it solves the puzzle.
        for (Circle c: circleList) {
            c.rotateLeft();
            solutionMoves++;
            totalMoves++;
            solved = solve(circleList);
            if (solved) {
                solution.push("Rotate " + c.getId() + " circle counter-clockwise.");
                return true;
            } else {
                c.rotateRight();
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
}