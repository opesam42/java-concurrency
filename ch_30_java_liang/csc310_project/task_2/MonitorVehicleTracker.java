package csc310_project.task_2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// Thread-Safe Implementation using the Java Monitor Pattern
public class MonitorVehicleTracker {
    
    // Encapsulated state: Private map of locations
    private final Map<String, MutablePoint> locations;

    // Constructor: Deep copies the initial data to ensure safety
    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    // Returns a SNAPSHOT of all locations (Deep Copy)
    public synchronized Map<String, MutablePoint> getLocations() {
        return deepCopy(locations);
    }

    // Returns a SNAPSHOT of one location
    public synchronized MutablePoint getLocation(String id) {
        MutablePoint loc = locations.get(id);
        return loc == null ? null : new MutablePoint(loc);
    }

    // Updates a location safely
    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint loc = locations.get(id);
        if (loc == null) {
            throw new IllegalArgumentException("No such ID: " + id);
        }
        loc.x = x;
        loc.y = y;
    }

    // Helper: Creates a deep copy of the map so the original is never exposed
    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
        Map<String, MutablePoint> result = new HashMap<>();
        for (String id : m.keySet()) {
            result.put(id, new MutablePoint(m.get(id)));
        }
        return Collections.unmodifiableMap(result);
    }
}
