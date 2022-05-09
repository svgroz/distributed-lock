package org.svgroz.dlock.api;

/**
 * @author Simon Grozovsky svgroz@outlook.com
 */
public interface LockStore {
    boolean acquire(LockData lockData);

    boolean delete(LockData lockData);
}
