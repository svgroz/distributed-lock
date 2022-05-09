package org.svgroz.dlock.api;

import java.util.function.Supplier;

/**
 * @author Simon Grozovsky svgroz@outlook.com
 */
public interface LockOperationTemplate {
    ExecutionResult<Void> execute(LockData lockData, Runnable operation);

    <T> ExecutionResult<T> execute(LockData lockData, Supplier<T> operation);
}
