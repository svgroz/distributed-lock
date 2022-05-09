package org.svgroz.dlock.redis.impl;

import org.svgroz.dlock.api.LockData;
import org.svgroz.dlock.api.LockStore;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Simon Grozovsky svgroz@outlook.com
 */
public class RedisLockStore implements LockStore {

    private final Supplier<Jedis> jedis;

    public RedisLockStore(final Supplier<Jedis> jedis) {
        this.jedis = Objects.requireNonNull(jedis);
    }

    @Override
    public boolean acquire(LockData lockData) {
        Objects.requireNonNull(lockData);
        if (lockData.getTtl().getSeconds() <= 0) {
            throw new IllegalArgumentException("TTL " + lockData.getTtl() + " is less than 1s");
        }

        String setResult = jedis.get().set(lockData.getLockId(), lockData.toString(), SetParams.setParams().nx().ex(lockData.getTtl().toSeconds()));
        return "OK".equals(setResult);
    }

    @Override
    public boolean delete(LockData lockData) {
        jedis.get().del(lockData.getLockId());
        return true;
    }
}
