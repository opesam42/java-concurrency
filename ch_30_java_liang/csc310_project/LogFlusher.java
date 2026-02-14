/* * PROJECT: The High-Throughput Log Recorder
 * * SCENARIO:
 * Imagine a busy backend server receiving thousands of requests per second. 
 * We need to record every error and warning for security audits, but we 
 * cannot afford to block the user's request while we save text to a file.
 * * ARCHITECTURE:
 * This simulation uses the Producer-Consumer principle to solve the "I/O Bottleneck."
 * * 1. The Producer represents the fast-moving application (generating data).
 * 2. The ArrayBlockingQueue acts as a thread-safe buffer (the waiting room).
 * 3. The Consumer represents the disk-writer thread, which processes the 
 * queue independently at its own pace.
 * * This pattern prevents data loss during traffic spikes while keeping the 
 * main system responsive.
 */

package csc310_project;

import java.util.concurrent.*;
import java.io.FileWriter;
import java.io.IOException;

public class LogFlusher {

    private static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new CollectLogsTask());
        executor.execute(new DumpLogsTask());

        executor.shutdown();
    }
 

    // producer - randomly produce logs
    private static class CollectLogsTask implements Runnable {
        @Override
        public void run() {
            String[] types = {"INFO", "WARN", "ERROR"};
            int id = 1;

            try {
                while (true) {
                    // 1. Create a fake log message
                    String type = types[(int)(Math.random() * 3)];
                    String log = type + ": Event ID #" + id++;

                    // 2. Put it in the queue
                    System.out.println("Generating: " + log);
                    queue.put(log);

                    // 3. Sleep briefly
                    Thread.sleep(1000); 
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // consumer
    private static class DumpLogsTask implements Runnable {
        @Override
        public void run(){
            try{
                while(true){
                    // take the head of the queue, as it uses FIFO
                    String log = queue.take();
                    System.out.println("The consumer reads " + log);
                    // write to log file
                    try{
                        FileWriter logFile = new FileWriter("logs.txt", true);
                        logFile.write(log + "\n");
                        logFile.close();
                    } catch (IOException e){
                        System.out.println("An error occured when writing to the log file" + e);
                    }

                    Thread.sleep(1000);
                }
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}