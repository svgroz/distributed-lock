package org.svgroz.dlock.api;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Simon Grozovsky svgroz@outlook.com
 */
public class LockData {
    private final String appId;
    private final String lockId;
    private final ZonedDateTime lockRequestTime;
    private final Duration timeout;
    private final Duration timeBetweenAcquireAttempts;
    private final Duration ttl;

    public LockData(
            final String appId,
            final String lockId,
            final ZonedDateTime lockRequestTime,
            final Duration timeout,
            final Duration timeBetweenAcquireAttempts,
            final Duration ttl
    ) {
        this.appId = Objects.requireNonNull(appId);
        this.lockId = Objects.requireNonNull(lockId);
        this.lockRequestTime = Objects.requireNonNullElseGet(lockRequestTime, ZonedDateTime::now);
        this.timeout = Objects.requireNonNull(timeout);
        this.timeBetweenAcquireAttempts = Objects.requireNonNull(timeBetweenAcquireAttempts);
        this.ttl = Objects.requireNonNull(ttl);
    }

    public String getAppId() {
        return appId;
    }

    public String getLockId() {
        return lockId;
    }

    public ZonedDateTime getLockRequestTime() {
        return lockRequestTime;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public Duration getTimeBetweenAcquireAttempts() {
        return timeBetweenAcquireAttempts;
    }

    public Duration getTtl() {
        return ttl;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LockData.class.getSimpleName() + "[", "]")
                .add("appId='" + appId + "'")
                .add("lockId='" + lockId + "'")
                .add("lockRequestTime=" + lockRequestTime)
                .add("timeout=" + timeout)
                .add("timeBetweenAcquireAttempts=" + timeBetweenAcquireAttempts)
                .add("ttl=" + ttl)
                .toString();
    }
}
