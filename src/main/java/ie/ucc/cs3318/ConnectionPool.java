package ie.ucc.cs3318;

public class ConnectionPool {
    private int connectionCounter;


    public ConnectionPool(int capacity) {
        if (capacity < 1 || capacity > 65535) {
            throw new IllegalArgumentException("capacity must be between 1 and 65,535");
        }

        Connection[] busy = new Connection[capacity];
        Connection[] available = new Connection[capacity];
    }
}
