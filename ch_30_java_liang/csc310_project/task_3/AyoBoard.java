package csc310_project.task_3;

import java.util.Arrays;

public class AyoBoard {
    private final int[] pits = new int[12];
    private int playerAScore = 0;
    private int playerBScore = 0;
    private int currentPlayer = 0; // 0 for Player A, 1 for Player B
    private boolean gameOver = false;

    public AyoBoard() {
        // Initialize 4 seeds in each pit
        Arrays.fill(pits, 4);
    }

    // The synchronized "Monitor" method
    public synchronized void makeMove(int playerID, int pitIndex) throws InterruptedException {
        // 1. Wait until it is this player's turn
        while (currentPlayer != playerID && !gameOver) {
            wait(); 
        }

        if (gameOver) return;

        System.out.println("Player " + (playerID == 0 ? "A" : "B") + " moves from pit " + pitIndex);

        // 2. Perform Sowing Logic
        int seeds = pits[pitIndex];
        pits[pitIndex] = 0;
        int lastPit = sowSeeds(pitIndex, seeds);

        // 3. Perform Capture Logic (Simplified)
        applyCapturingRules(playerID, lastPit);

        // 4. Check if game is over (Total seeds captured > 24 or no moves left)
        if (playerAScore + playerBScore >= 48 || playerAScore > 24 || playerBScore > 24) {
            gameOver = true;
            System.out.println("GAME OVER! Scores: A=" + playerAScore + " B=" + playerBScore);
        }

        // 5. Switch turn and wake up the other thread
        currentPlayer = (playerID == 0) ? 1 : 0;
        notifyAll();
    }

    private int sowSeeds(int startPit, int seeds) {
        int current = startPit;
        while (seeds > 0) {
            current = (current + 1) % 12;
            // Special Rule: Skip the starting pit if more than 12 seeds
            if (current == startPit) continue; 
            pits[current]++;
            seeds--;
        }
        return current;
    }


    private void applyCapturingRules(int playerID, int lastPit) {
        int current = lastPit;

        // 1. Identify the opponent's territory
        // If playerID is 0 (A), opponent is 6-11. 
        // If playerID is 1 (B), opponent is 0-5.
        int opponentStart = (playerID == 0) ? 6 : 0;
        int opponentEnd = (playerID == 0) ? 11 : 5;

        // 2. Start the backward chain check
        while (true) {
            // RULE: You can only capture from the opponent's side
            if (current < opponentStart || current > opponentEnd) {
                break; 
            }

            // RULE: Capture only if the pit now contains exactly 2 or 3 seeds
            if (pits[current] == 2 || pits[current] == 3) {
                int capturedSeeds = pits[current];
                pits[current] = 0; // Empty the pit

                // Add to the correct player's score
                if (playerID == 0) {
                    playerAScore += capturedSeeds;
                } else {
                    playerBScore += capturedSeeds;
                }

                // Move BACKWARDS to the previous pit (Clockwise)
                // We add 11 and % 12 to safely move back one index in a circle
                current = (current + 11) % 12;
            } else {
                // Stop as soon as a pit doesn't meet the 2 or 3 seed requirement
                break;
            }
        }
    }

    public synchronized boolean isGameOver() {
        return gameOver;
    }

    // helper methods
    public synchronized int getSeedCount(int pitIndex){
        return pits[pitIndex];
    }
}