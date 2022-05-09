package org.svgroz.dlock.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.svgroz.dlock.api.ExecutionResult;
import org.svgroz.dlock.api.LockData;
import org.svgroz.dlock.api.LockOperationTemplate;
import org.svgroz.dlock.api.LockStore;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Simon Grozovsky svgroz@outlook.com
 */
public class DefaultLockOperationTemplate implements LockOperationTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLockOperationTemplate.class);

    private final LockStore lockStore;

    public DefaultLockOperationTemplate(final LockStore lockStore) {
        this.lockStore = Objects.requireNonNull(lockStore);
    }

    @Override
    public ExecutionResult<Void> execute(
            final LockData lockData,
            final Runnable operation
    ) {
        return execute(
                lockData,
                () -> {
                    operation.run();
                    return null;
                }
        );
    }

    @Override
    public <T> ExecutionResult<T> execute(
            final LockData lockData,
            final Supplier<T> operation
    ) {
        Objects.requireNonNull(lockData);
        Objects.requireNonNull(operation);

        final LocalDateTime maxTimeout = LocalDateTime.now().plus(lockData.getTimeout());

        for (int i = 1; ; i++) {
            LOGGER.debug("Attempts to acquire lock: {}", i);
            final boolean isAcquired;
            try {
                isAcquired = lockStore.acquire(lockData);
            } catch (Exception ex) {
                LOGGER.debug("Could not acquire lock, store has thrown exception", ex);
                return new ExecutionResult<>(false, ex, null);
            }

            if (isAcquired) {
                final T result = operation.get();
                try {
                    lockStore.delete(lockData);
                } catch (Exception ex) {
                    LOGGER.debug("Could not delete lock, store has thrown exception", ex);
                    return new ExecutionResult<>(true, ex, result);
                }
                return new ExecutionResult<>(true, null, result);
            }

            final LocalDateTime now = LocalDateTime.now();
            long millsToSleep = (now.plus(lockData.getTimeBetweenAcquireAttempts()).isBefore(maxTimeout)
                    ? lockData.getTimeBetweenAcquireAttempts()
                    : Duration.between(now, maxTimeout)).toMillis();
            if (millsToSleep < 1) {
                LOGGER.debug("Max timeout has been reached and lock is not acquired");
                return new ExecutionResult<>(false, null, null);
            }

            try {
                LOGGER.debug("Sleep for {}mills", millsToSleep);
                Thread.sleep(millsToSleep);
            } catch (InterruptedException ex) {
                LOGGER.warn("Thread was interrupted for lock: {}", lockData);
            }
        }
    }
}
