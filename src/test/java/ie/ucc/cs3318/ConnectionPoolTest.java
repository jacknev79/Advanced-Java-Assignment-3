package ie.ucc.cs3318;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConnectionPoolTest {

    @Test
    void throwsExceptionWhenCapacityTooSmall() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ConnectionPool(0)
        );

        assertTrue(ex.getMessage().contains("between 1 and 65,535"));
    }

    @Test
    void throwsExceptionWhenCapacityTooLarge() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ConnectionPool(65536)
        );

        assertTrue(ex.getMessage().contains("between 1 and 65,535"));
    }

    @Test
    void testCapacityWithinRange() {
        assertDoesNotThrow(() -> new ConnectionPool(1));
        assertDoesNotThrow(() -> new ConnectionPool(65535));
    }

    @Test
    void testReleaseConnectionIdOutOfRange() {
        ConnectionPool pool = new ConnectionPool(10);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> pool.release(11));
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> pool.release(-1));
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> pool.release(100000));

        assertTrue(e.getMessage().contains("positive and less than 10"));
        assertTrue(e1.getMessage().contains("positive and less than 10"));
        assertTrue(e2.getMessage().contains("positive and less than 10"));
    }

    @Test
    void testReleaseNullConnection() {
        ConnectionPool pool = new ConnectionPool(10);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> pool.release(4));

        assertTrue(e.getMessage().contains("4 not found"));
    }

    @Test
    void testRequestWithinCapacity() {
        ConnectionPool pool = new ConnectionPool(10);
        assertDoesNotThrow(() -> pool.request());
        assertDoesNotThrow(() -> pool.request());
    }

    @Test
    void testRequestAboveCapacity() {
        ConnectionPool pool = new ConnectionPool(2);
        Connection c = pool.request();
        Connection c1 = pool.request();

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> pool.request());

        assertTrue(e.getMessage().contains("No connections available"));
    }

    @Test
    void testRequestReleaseRequest() {
        ConnectionPool pool = new ConnectionPool(1);
        Connection c = pool.request();
        pool.release(c.getId());
        assertDoesNotThrow(() -> pool.request());
    }

}
