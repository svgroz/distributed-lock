package org.svgroz.dlock.redis.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.svgroz.dlock.api.LockData;
import redis.clients.jedis.JedisPool;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Simon Grozovsky svgroz@outlook.com
 */
class RedisLockStoreTest {

    private JedisPool jedisPool;

    @BeforeEach
    public void before() throws Exception {
        this.jedisPool = new JedisPool();
    }

    @AfterEach
    public void after() throws Exception {
        if (this.jedisPool == null || this.jedisPool.isClosed()) {
            return;
        }

        this.jedisPool.getResource().flushDB();
        this.jedisPool.close();
        this.jedisPool = null;
    }

    @Test
    void acquire() throws Exception {
        final JedisPool jedisPool = this.jedisPool;
        RedisLockStore redisLockStore = new RedisLockStore(() -> jedisPool.getResource());

        String name = UUID.randomUUID().toString();
        LockData testLock = new LockData(
                name,
                UUID.randomUUID().toString(),
                ZonedDateTime.now(),
                Duration.ofSeconds(3),
                Duration.ofMillis(100),
                Duration.ofSeconds(1)

        );
        boolean isAcquired = redisLockStore.acquire(testLock);
        Assertions.assertTrue(isAcquired);
        isAcquired = redisLockStore.acquire(testLock);
        Assertions.assertFalse(isAcquired);
        Thread.sleep(1000);
        isAcquired = redisLockStore.acquire(testLock);
        Assertions.assertTrue(isAcquired);
    }
}