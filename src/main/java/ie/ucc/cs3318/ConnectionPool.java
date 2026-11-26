package ie.ucc.cs3318;

import java.util.ArrayList;

public class ConnectionPool {
    private int connectionCounter;
    private Connection[] busy;
    private ArrayList<Connection> available;
    private int capacity;

    public ConnectionPool(int capacity) {
        if (capacity < 1 || capacity > 65535) {
            throw new IllegalArgumentException("capacity must be between 1 and 65,535");
        }
        this.connectionCounter = 0;
        this.capacity = capacity;
        this.busy = new Connection[capacity];
        this.available = new ArrayList<>();
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
        this.available.add(c);
        this.busy[id] = null;
    }

    public Connection request() {
        if (this.connectionCounter >= capacity && this.available.isEmpty()) {
            throw new IllegalStateException("No connections available");
        }

        Connection c;
        if (this.available.isEmpty()) {
            c = new Connection(this.connectionCounter);
            this.connectionCounter++;
        }
        else {
            c = this.available.removeLast();
        }

        c.setAvailable(false);
        if (busy[c.getId()] != null) {
            // NB cannot test without being able to access
            // private busy field in ConnectionPoolTest.java,
            // therefore will not test this exception
            throw new IllegalStateException("Multiple connections sharing the same id, " +
                    "should not be allowed.");
        }
        busy[c.getId()] = c;
        return c;
    }

    public int[] getStatus() {
        // should return an array of capacity, connections in use, connections available
        int[] status = new int[3];
        status[0] = this.capacity;
        status[1] = this.connectionCounter - this.available.size();
        status[2] = this.available.size();

        return status;
    }

}
