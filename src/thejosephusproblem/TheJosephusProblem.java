/*
 * This program is designed to check the Josephus Problem
 * "If n soldiers sit in a ring, and in turn each soldier kill the 
 * soldier to his left, which seat is the soldier left alive?"
 */
package thejosephusproblem;

/**
 *
 * @author MagnusJ
 */
public class TheJosephusProblem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Init this class
        TheJosephusProblem tjp = new TheJosephusProblem();
        
        /**
         * First test the first few iterations of the problem, to see if any
         * patterns emerge.
         */
        System.out.println("Test #1: Just test the problem for a few number of soldiers");
        tjp.tryProblem();
        
        /**
         * From the first test, running through 0-18 soldiers, I have observed
         * that the solutions resets to 1 on 1, 2, 4, and 8, which is powers of
         * 2, and increase by 2 every number in between.
         * 
         * Next is to test powers of 2, and then check in between.
         */
        System.out.println("");
        System.out.println("Test #2: Test powers of 2, to check if they are indeed always 1");
        tjp.testPowersOfTwo();
        
        /**
         * The check on powers of 2 were positive, as they always resulted in 1.
         * Now onto check the numbers in between.
         */
        System.out.println("");
        System.out.println("Test #3: Test numbers between powers of 2, to see if they always increase by 2");
        tjp.testInBetweeners();
        
        /**
         * The numbers check out between 32 and 64, and it seems that the 
         * patterns are "Resets to 1 at 2^x" and "Increases by 2 each time a new
         * soldier is added to the troop".
         * 
         * Solution must be to remove the closest power of 2 below, and check
         * what is left.
         */
        System.out.println("");
        System.out.println("Test #4: Remove 2^a (closest power of 2 below total number), and check what is left");
        tjp.testPowerLess();
        
        /**
         * The powerless number (lets call them l) are half of the number below
         * the the answer, which would mean that the answer is 2*l + 1. So,
         * given the total number of soldiers n, and n = 2^a + l, where
         * 2^a > l, the safe spot to sit in is 2l + 1.
         * 
         * Next up, it's time to try solving some problems using both the old
         * brute force method, and the new formula.
         */
        System.out.println("");
        System.out.println("Test #5: Try the new formula, and compare it to the known results");
        tjp.testFormula();
        
        /**
         * The formula works wonders, so now it's time to tackle the real
         * problem: In Josephus' troop there were 41 soldiers, including
         * Josephus himself. In which position should Josephus position himself
         * to survive and surrender?
         */
        System.out.println("");
        System.out.println("Test #6: Solve Josephus' Problem, with 41 soldiers, where should Josephus sit?");
        tjp.solveJosephusProblem();
    }
    
    // First test out the problem "testCases" times, and print number of 
    // soldiers and winning seat, to see if there's a pattern
    private void tryProblem() {
        int caseNums = 19;
        int[] testCases = new int[caseNums];
        for (int i = 0; i < caseNums; i++) {
            testCases[i] = i;
        }
        solveTestCases(testCases);
    }
    
    // Take a set number of test cases, solve them, and print the results
    private void solveTestCases(int[] testCases) {
        int testCaseLength = testCases.length;
        int[][] winnerSeat = new int[testCaseLength][2];
        for (int i = 0; i < testCaseLength; i++) {
            winnerSeat[i][0] = testCases[i];
            winnerSeat[i][1] = solveProblemBrute(testCases[i]);
        }
        printSolution(winnerSeat);
    }
    
    // Brute force the solution for the Josephus Problem for "soldiers" number of soldiers
    private int solveProblemBrute(int soldiers) {
        // Set up a circle (or row) of living soldiers to the specified size of "soldiers",
        // or return 0 if there are no soldiers
        if (soldiers < 1) return 0;
        boolean[] livingSoldiers = new boolean[soldiers];
        for (int i = 0; i < soldiers; i++) {
            livingSoldiers[i] = true;
        }
        
        // Initiate the temporary variables
        // "currentSoldier" is which soldier next to kill his left hand man, starts on 0
        // "nextSoldier" is how many spots to the left of "currentSoldier" the next soldier sits
        int currentSoldier = 0;
        int nextSoldier = 1;
        
        // Loop through the circle, find next soldier and kill his left hand man until last man standing
        while (true) {
            // Check if current soldier is alive, if not try soldier to the left
            if (!livingSoldiers[currentSoldier]) {
                currentSoldier = (currentSoldier + 1) % soldiers;
            // Check if there are now other soldiers left
            } else if (nextSoldier >= soldiers) {
                return currentSoldier+1;
            // Check if "nextSoldier" is alive, if not check next soldier on left
            } else if (!livingSoldiers[(currentSoldier + nextSoldier) % soldiers]) {
                nextSoldier++;
            // If everything is ok, kill the next soldier, and set "currentSoldier" to next soldier on left
            } else {
                livingSoldiers[(currentSoldier + nextSoldier) % soldiers] = false;
                currentSoldier = (currentSoldier + nextSoldier + 1) % soldiers;
                nextSoldier = 1;
            }
        }
    }
    
    // Prints the solution to console in a nice table
    // TODO: Perhaps write another which uses ArrayList
    private void printSolution(int[][] solutions) {
        String topLine = "Soldiers | Seats";
        String splitLine = "---------+------";
        String divider = " | ";
        String padding = "       ";
        System.out.println("" + topLine);
        System.out.println("" + splitLine);
        for (int i = 0; i < solutions.length; i++) {
            if (solutions[i][0] >= 10) padding = "      ";
            if (solutions[i][0] >= 100) padding = "     ";
            System.out.println("" + padding + solutions[i][0] + divider + solutions[i][1]);
        }
    }
    
    // A second print function to accomodate numbers with the closest power removed
    private void printSolution(int[][] solutions, boolean powerless) {
        String topLine = "Soldiers | Seats | Formulaic";
        if (powerless) topLine = "Soldiers | Seats | Unpowered";
        String splitLine = "---------+-------+----------";
        String divider = " | ";
        String padding1 = "       ";
        String padding2 = "    ";
        System.out.println("" + topLine);
        System.out.println("" + splitLine);
        for (int i = 0; i < solutions.length; i++) {
            if (solutions[i][0] >= 10) padding1 = "      ";
            if (solutions[i][0] >= 100) padding1 = "     ";
            if (solutions[i][1] < 10) padding2 = "    ";
            if (solutions[i][1] >= 10) padding2 = "   ";
            if (solutions[i][1] >= 100) padding2 = "  ";
            System.out.println("" + padding1 + solutions[i][0] + divider + solutions[i][1] + padding2 + divider + solutions[i][2]);
        }
    }
    
    // Test only powers of 2
    private void testPowersOfTwo() {
        int testPowers = 10;
        int[] testCases = new int[testPowers];
        for (int i = 0; i < 10; i++) {
            testCases[i] = (int)Math.pow(2, i);
        }
        solveTestCases(testCases);
    }
    
    // Test numbers between powers of 2
    private void testInBetweeners() {
        int testStartAndLength = 32;
        int[] testCases = new int[testStartAndLength];
        for (int i = 0; i < testStartAndLength; i++) {
            testCases[i] = testStartAndLength + i;
        }
        solveTestCases(testCases);
    }
    
    // Finds the answer of some cases with the closest power of 2 removed
    private void testPowerLess() {
        int testCases = 30;
        int[][] winnerSeat = new int[testCases][3];
        for (int i = 0; i < testCases; i++) {
            winnerSeat[i][0] = i;
            winnerSeat[i][1] = solveProblemBrute(i);
            winnerSeat[i][2] = remClosePower(i);
        }
        printSolution(winnerSeat, true);
    }
    
    // Removes the closest power of two below the number
    private int remClosePower(int powered) {
        if (powered <= 2) return 0;
        boolean powerOver = false;
        int currentPower = 2;
        int nextPower = currentPower * 2;
        while (!powerOver) {
            if (nextPower > powered) {
                powerOver = true;
            } else {
                currentPower = nextPower;
                nextPower *= 2;
            }
        }
        return powered - currentPower;
    }
    
    // Test some cases using the new formula
    private void testFormula() {
        int testCases = 30;
        int[][] winnerSeat = new int[testCases][3];
        for (int i = 0; i < testCases; i++) {
            winnerSeat[i][0] = i;
            winnerSeat[i][1] = solveProblemBrute(i);
            winnerSeat[i][2] = solveProblemFormula(i);
        }
        printSolution(winnerSeat, false);
    }
    
    // Solve the problem using the formula "If n = 2^a + l, where l < 2^a, then W(n) = 2l + 1"
    private int solveProblemFormula(int soldiers) {
        if (soldiers < 1) return 0;
        int unpowered = remClosePower(soldiers);
        return 2*unpowered + 1;
    }
    
    // Solve Josephus' Problem
    private void solveJosephusProblem() {
        int solution = solveProblemFormula(41);
        System.out.println("\t--> Josephus must sit in position " + solution + " to survive and surrender. <--");
    }
    
}
