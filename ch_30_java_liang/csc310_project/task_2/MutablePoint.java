package csc310_project.task_2;

// Represents a location (x, y). NOT thread-safe on its own.
public class MutablePoint {
    public int x, y;

    public MutablePoint() { 
        x = 0; y = 0; 
    }

    // Copy Constructor
    public MutablePoint(MutablePoint p) {
        this.x = p.x;
        this.y = p.y;
    }
}