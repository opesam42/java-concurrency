package csc310_project.task_2;

import java.util.HashMap;
import java.util.Map;

public class FleetDemo {
    public static void main(String[] args) {
        // data setup
        Map<String, MutablePoint> initialMap = new HashMap<>();
        MutablePoint p1 = new MutablePoint(); p1.x = 0; p1.y = 0;
        
        initialMap.put("Taxi-001", p1);

        MonitorVehicleTracker tracker = new MonitorVehicleTracker(initialMap);

        // thread to simulate a GPS updating the location
        Thread updater = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    int newX = i * 10;
                    int newY = i * 10;
                    tracker.setLocation("Taxi-001", newX, newY);
                    System.out.println("[GPS Update] Taxi moved to: " + newX + ", " + newY);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        });

        // thread to simulate a Dispatcher viewing the map
        Thread viewer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    Map<String, MutablePoint> snapshot = tracker.getLocations();
                    MutablePoint taxi = snapshot.get("Taxi-001");
                    System.out.println("[Dispatcher View] Taxi is at: " + taxi.x + ", " + taxi.y);
                    Thread.sleep(600); // Checks slightly slower than updates
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        });

        updater.start();
        viewer.start();
    }
}