package ie.ucc.cs3318.internal;

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
        Connection c1 = new Connection(11);
        Connection c2 = new Connection(-1);
        Connection c3 = new Connection(100000);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> pool.release(c1));
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> pool.release(c2));
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> pool.release(c3));

        assertTrue(e.getMessage().contains("positive and less than 10"));
        assertTrue(e1.getMessage().contains("positive and less than 10"));
        assertTrue(e2.getMessage().contains("positive and less than 10"));
    }

    @Test
    void testReleaseNullConnection() {
        ConnectionPool pool = new ConnectionPool(10);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> pool.release(null));

        assertTrue(e.getMessage().contains("c cannot be null"));
    }

    @Test
    void testReleaseConnectionTwice() {
        ConnectionPool pool = new ConnectionPool(10);
        Connection c = pool.request();
        pool.release(c);
        assertThrows(IllegalStateException.class, () -> pool.release(c));
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
        pool.release(c);

        Connection c2 = assertDoesNotThrow(() -> pool.request());

        assertEquals(c.getId(), c2.getId());
    }

    @Test
    void testPoolStatusCorrectlyReturned1() {
        ConnectionPool pool = new ConnectionPool(3);
        Connection c = pool.request();
        Connection c1 = pool.request();
        Connection c2 = pool.request();
        pool.release(c);

        int[] status = pool.getStatus();

        assertEquals(3, status[0]);
        assertEquals(2, status[1]);
        assertEquals(1, status[2]);

    }

    @Test
    void testPoolStatusCorrectlyReturned2() {
        ConnectionPool pool = new ConnectionPool(5);
        Connection c = pool.request();
        Connection c1 = pool.request();
        Connection c2 = pool.request();
        Connection c3 = pool.request();
        pool.release(c);
        pool.release(c3);
        Connection c4 = pool.request();

        int[] status = pool.getStatus();

        assertEquals(5, status[0]);
        assertEquals(3, status[1]);
        assertEquals(1, status[2]);

    }

}
