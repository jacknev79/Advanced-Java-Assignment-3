package ie.ucc.cs3318;

public class Connection {
    private boolean available;

    public Connection() {
        available = true;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
