package org.svgroz.dlock.api;

/**
 * @author Simon Grozovsky svgroz@outlook.com
 */
public class ExecutionResult<T> {
    private final boolean executed;
    private final Exception lockingException;
    private final T result;

    public ExecutionResult(final boolean executed, final Exception lockingException, final T result) {
        this.executed = executed;
        this.lockingException = lockingException;
        this.result = result;
    }

    public boolean isExecuted() {
        return executed;
    }

    public Exception getLockingException() {
        return lockingException;
    }

    public T getResult() {
        return result;
    }
}
