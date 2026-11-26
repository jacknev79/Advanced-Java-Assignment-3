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
        assertThrows(IllegalArgumentException.class, () -> pool.release(11));
        assertThrows(IllegalArgumentException.class, () -> pool.release(0));
        assertThrows(IllegalArgumentException.class, () -> pool.release(100000));
    }
}
