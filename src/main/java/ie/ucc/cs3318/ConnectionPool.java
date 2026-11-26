package ie.ucc.cs3318;

public class ConnectionPool {
    private int connectionCounter;
    private Connection[] busy;
    private Connection[] available;
    private int capacity;

    public ConnectionPool(int capacity) {
        if (capacity < 1 || capacity > 65535) {
            throw new IllegalArgumentException("capacity must be between 1 and 65,535");
        }

        this.capacity = capacity;
        this.busy = new Connection[capacity];
        this.available = new Connection[capacity];
    }

    public void release(int id) {
        if (id < 0 || id >= capacity) {
            throw new IllegalArgumentException("id must positive and less than " + capacity);
        }
        Connection c = busy[id];
        if (c == null) {
            throw new IllegalArgumentException("Connection with id " + id + " not found");
        }
        c.setAvailable(true);
        this.available[id] = c;
        this.busy[id] = null;
    }

}
