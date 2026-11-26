package ie.ucc.cs3318.internal;

public class Connection {
    private boolean available;
    private int id;

    public Connection(int id) {
        available = true;
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getId() { return id; }
}
