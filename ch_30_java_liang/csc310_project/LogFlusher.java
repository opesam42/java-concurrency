/* * PROJECT: The Hight Throughput Batch Log Recorder
 * * SCENARIO: 
 * High-traffic servers can't afford to wait for slow disk writes. We need 
 * an asynchronous way to save logs without slowing down the application.
 * * ARCHITECTURE (Timed-Batch Producer-Consumer):
 * 1. PRODUCER: Generates logs and puts them in a thread-safe buffer.
 * 2. BUFFER: A "Loading Dock" using ReentrantLocks and Conditions to 
 * coordinate threads and prevent data corruption.
 * 3. BATCH CONSUMER: Drains the entire queue in one scoop. This reduces 
 * Disk I/O overhead by writing many logs with a single file-open call.
 * 4. TIMED FLUSH: Uses a timeout (e.g., 5s) so that during slow traffic, 
 * logs aren't "stuck" in memory waiting for the buffer to fill up.
 */


package csc310_project;

import java.util.concurrent.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.List;
import java.util.ArrayList;

public class LogFlusher {

    // private static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
    private static Buffer buffer = new Buffer();

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
                    buffer.write(log);

                    // make the producer faster than the consumer to clearly see the log flushing
                    Thread.sleep((int)(Math.random() * 500));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // consumer - take log and save to a file
    private static class DumpLogsTask implements Runnable {
        @Override
        public void run(){
            try{
                while(true){
                    List<String> logs = buffer.readBatch(5000);
                    if (!logs.isEmpty()) {
                        System.out.println("Flushing batch of size: " + logs.size());

                        try (FileWriter logFile = new FileWriter("logs.txt", true)) {
                            for (String log : logs) {
                                logFile.write(log + "\n");
                            }
                        } catch (IOException e) {
                            System.err.println("Disk Error: " + e.getMessage());
                        }
                    }
                    // make consumer slightly slower than producer 
                    Thread.sleep((int)(Math.random() * 1000));
                }
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }



    private static class Buffer {
        private static final int CAPACITY = 3;
        private final Queue<String> queue = new LinkedList<>();


        private static Lock lock = new ReentrantLock();
        private static Condition notEmpty = lock.newCondition();
        private static Condition notFull = lock.newCondition();

        public void write(String value) throws InterruptedException {
            lock.lock();
            try {
                while (queue.size() == CAPACITY) {
                    System.out.println("Buffer full. Waiting...");
                    notFull.await();
                }

                queue.offer(value);
                notEmpty.signal(); 
            } finally {
                lock.unlock();
            }
        }

        public List<String> readBatch(long timeout) throws InterruptedException {
            lock.lock();
            // wait until there is at least one log is reached or time is out
            try {
                while (queue.isEmpty()) {
                    notEmpty.await(timeout, TimeUnit.MILLISECONDS);
                }

                // Collect everything currently in the queue
                List<String> batch = new ArrayList<>(queue);
                queue.clear();

                notFull.signal();
                return batch;
            } finally {
                lock.unlock();
            }
        }

        public String read() throws InterruptedException {
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    System.out.println("Buffer empty. Waiting...");
                    notEmpty.await();
                }

                String value = queue.poll();
                notFull.signal();
                return value;
            } finally {
                lock.unlock();
            }
        }
    }
}