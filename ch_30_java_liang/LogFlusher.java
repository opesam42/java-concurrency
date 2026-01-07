import java.util.*;
import java.io.*;

public class LogFlusher implements Runnable {
    // This is our "Buffer" where logs are temporarily stored in memory
    private static List<String> logBuffer = Collections.synchronizedList(new ArrayList<>());
    private final int FLUSH_INTERVAL = 5000; // 5 seconds (The "Sleep" time)

    public static void addLog(String message) {
        logBuffer.add(new Date() + ": " + message);
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 1. The "Sleep" - Just like the flashing text delay
                Thread.sleep(FLUSH_INTERVAL);

                // 2. The "Action" - Flush the buffer to a file
                if (!logBuffer.isEmpty()) {
                    flushToFile();
                }
            }
        } catch (InterruptedException ex) {
            System.out.println("Log Flusher shutting down..."); //
        }
    }

    private void flushToFile() {
        System.out.println("--- Background Thread: Flushing logs to disk ---");
        try (PrintWriter writer = new PrintWriter(new FileWriter("app.log", true))) {
            synchronized (logBuffer) {
                for (String log : logBuffer) {
                    writer.println(log);
                }
                logBuffer.clear(); // Empty the buffer after flushing
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Start the background flusher thread
        Thread flusher = new Thread(new LogFlusher());
        flusher.setDaemon(true); // Ensures the thread dies when the app closes
        flusher.start();

        // Simulate the main application (Backend API) receiving requests
        for (int i = 1; i <= 10; i++) {
            addLog("User request #" + i);
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            System.out.println("Main App: Added log " + i);
        }
    }
}