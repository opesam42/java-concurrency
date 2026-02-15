package csc310_project.task_3;

import java.util.Random;

public class AyoPlayer implements Runnable {
    private final AyoBoard board;
    private final int playerID; // 0 or 1
    private final Random random = new Random();

    public AyoPlayer(AyoBoard board, int playerID) {
        this.board = board;
        this.playerID = playerID;
    }

    @Override
    public void run() {
        try {
            while (!board.isGameOver()) {
                // Decide which pit to pick (0-5 for A, 6-11 for B)
                int offset = (playerID == 0) ? 0 : 6;
                int chosenPit = offset + random.nextInt(6);

                if (board.getSeedCount(chosenPit) > 0){
                    // Attempt to make a move via the Monitor
                    board.makeMove(playerID, chosenPit);

                    // Simulate "thinking" time
                    Thread.sleep(800);
                } else {
                    // let it loop again to get a random pit
                    continue;
                }

                
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}