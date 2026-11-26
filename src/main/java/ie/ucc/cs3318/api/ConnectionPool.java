package ie.ucc.cs3318.api;

import ie.ucc.cs3318.internal.Connection;

public interface ConnectionPool {

    abstract int getCapacity();

    abstract int getAvailable();

    abstract int getInUse();

    abstract int getTotal();

    abstract void release(Connection c);

    abstract Connection request();

    abstract int[] getStatus();
}
