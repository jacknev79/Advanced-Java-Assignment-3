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
    void testRequestWithinRange() {
        ConnectionPool pool = new ConnectionPool(10);
        assertDoesNotThrow(() -> pool.request(1));
        assertDoesNotThrow(() -> pool.request(10));

    }

    @Test
    void testRequestAboveCapacity() {
        ConnectionPool pool = new ConnectionPool(10);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> pool.request(11));

        assertTrue(e.getMessage().contains(""));
    }

    @Test
    void testRequestBelowCapacity() {
        ConnectionPool pool = new ConnectionPool(10);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> pool.release(-1));

        assertTrue(e.getMessage().contains(""));
    }
}
