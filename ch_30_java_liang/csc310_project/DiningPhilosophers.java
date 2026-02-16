package csc310_project;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class DiningPhilosophers {
    private static final int N = 5;  // Number of philosophers
    private static final Semaphore[] chopstick = new Semaphore[N];

    public static void main(String[] args) {
        // Initialize chopstick semaphores
        for (int i = 0; i < N; i++) {
            chopstick[i] = new Semaphore(1, true);      // setting fair=true ensure that starvation is handled as it ensure taht the thread are served in the order that they requested for them
        }

        // Create philosopher threads
        for (int i = 0; i < N; i++) {
            new Thread(new Philosopher(i)).start();
        }
    }

    static class Philosopher implements Runnable {
        private final int id;

        Philosopher(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try{
                while (true) {
                    System.out.println("Philosopher " + id + " is thinking...");
                    sleep(1);

                    int left_fork = id;
                    int right_fork = (id + 1) % N;

                    int first_id, second_id;
                    Semaphore first_stick, second_stick;
                    
                    // Philosopher always pick the up the fork with the lowest id first
                    if (left_fork < right_fork){
                        first_id = left_fork;
                        second_id = right_fork;

                        first_stick = chopstick[left_fork];
                        second_stick = chopstick[right_fork];
                    } else{
                        // For Phil 4, Left is 4 and Right is 0.
                        // Since 0 < 4, Phil 4 must pick up the Right Fork (0) first, breaking the deadlock cycle.
                        first_id = right_fork;
                        second_id = left_fork;

                        first_stick = chopstick[right_fork];
                        second_stick = chopstick[left_fork];
                    }

                    // Pick up first
                    first_stick.acquire();
                    // Pick up the second
                    second_stick.acquire();

                    System.out.println("Philosopher " + id + " picks up the sticks " + first_id + " and " + second_id + " and is started eating");
                    sleep(2);

                    // Put down sticks
                    System.out.println("Philosopher " + id + " releases up the sticks " + first_id + " and " + second_id + " and has finished eating");
                    second_stick.release();
                    first_stick.release();
                }
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        private void sleep(int seconds) {
            try { 
                TimeUnit.SECONDS.sleep(seconds); 
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }
        }
    }
}