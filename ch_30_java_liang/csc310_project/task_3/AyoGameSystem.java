package csc310_project.task_3;

public class AyoGameSystem {
    public static void main(String[] args) {
        AyoBoard sharedBoard = new AyoBoard();

        Thread playerA = new Thread(new AyoPlayer(sharedBoard, 0), "Thread-PlayerA");
        Thread playerB = new Thread(new AyoPlayer(sharedBoard, 1), "Thread-PlayerB");

        System.out.println("Starting Ayo Game...");
        playerA.start();
        playerB.start();
    }
}

