package csc310_project.task_3;

import java.util.Arrays;

public class AyoBoard {
    private final int[] pits = new int[12];
    private int playerAScore = 0;
    private int playerBScore = 0;
    private int currentPlayer = 0; 
    private boolean gameOver = false;

    public AyoBoard() {
        Arrays.fill(pits, 4);
        System.out.println("SYSTEM: Game initialized. 48 seeds distributed across 12 pits.");
    }

    public synchronized void makeMove(int playerID, int pitIndex) throws InterruptedException {
        
        while (currentPlayer != playerID && !gameOver) {
            wait(); 
        }

        if (gameOver) return;

        String name = (playerID == 0 ? "Player A" : "Player B");
        int seedsToSow = pits[pitIndex];
        
        // 2. Initial Move Reporting
        System.out.println("\nACTION: " + name + " selects pit " + pitIndex + " containing " + seedsToSow + " seeds.");

        pits[pitIndex] = 0;
        int lastPit = sowSeeds(pitIndex, seedsToSow);
        
        System.out.println("STATUS: Sowing complete. Final seed placed in pit " + lastPit + ".");

        // report seed capture
        int scoreBefore = (playerID == 0) ? playerAScore : playerBScore;
        applyCapturingRules(playerID, lastPit);
        int scoreAfter = (playerID == 0) ? playerAScore : playerBScore;

        if (scoreAfter > scoreBefore) {
            int gained = scoreAfter - scoreBefore;
            System.out.println("CAPTURE: " + name + " successfully harvested " + gained + " seeds from opponent territory.");
        } else {
            System.out.println("STATUS: No seeds were eligible for capture this turn.");
        }

        // log game summary
        printScoreSummary();
        renderBoardMap();

        // check for termination
        if (playerAScore >= 25 || playerBScore >= 25 || (playerAScore + playerBScore >= 48)) {
            gameOver = true;
            printFinalResults();
        }

        // give permission for next player to play
        currentPlayer = (playerID == 0) ? 1 : 0;
        notifyAll();
    }

    private void printScoreSummary() {
        System.out.println("CURRENT SCOREBOARD: Player A [" + playerAScore + "] | Player B [" + playerBScore + "]");
    }

    private void renderBoardMap() {
        System.out.print("BOARD STATE (Pits 0-11): ");
        for (int i = 0; i < 12; i++) {
            if (i == 6) System.out.print("| "); 
            System.out.print(pits[i] + " ");
        }
        System.out.println();
    }

    private void printFinalResults() {
        System.out.println("\nTERMINATION: Game Over condition met.");
        System.out.println("FINAL TOTALS: A: " + playerAScore + " | B: " + playerBScore);
        if (playerAScore > playerBScore) {
            System.out.println("RESULT: Player A is the winner.");
        } else if (playerBScore > playerAScore) {
            System.out.println("RESULT: Player B is the winner.");
        } else {
            System.out.println("RESULT: The match ended in a draw.");
        }
    }

    // Existing sowSeeds and applyCapturingRules logic...
    private int sowSeeds(int startPit, int seeds) {
        int current = startPit;
        while (seeds > 0) {
            current = (current + 1) % 12;
            if (current == startPit) continue; 
            pits[current]++;
            seeds--;
        }
        return current;
    }

    private void applyCapturingRules(int playerID, int lastPit) {
        int current = lastPit;
        int opponentStart = (playerID == 0) ? 6 : 0;
        int opponentEnd = (playerID == 0) ? 11 : 5;

        while (true) {
            if (current < opponentStart || current > opponentEnd) break; 
            if (pits[current] == 2 || pits[current] == 3) {
                int captured = pits[current];
                pits[current] = 0;
                if (playerID == 0) playerAScore += captured;
                else playerBScore += captured;
                current = (current + 11) % 12;
            } else {
                break;
            }
        }
    }

    public synchronized boolean isGameOver() { return gameOver; }
    public synchronized int getSeedCount(int pitIndex) { return pits[pitIndex]; }
}