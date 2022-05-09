package org.svgroz.dlock.api.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.svgroz.dlock.api.ExecutionResult;
import org.svgroz.dlock.api.LockData;
import org.svgroz.dlock.api.LockStore;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Simon Grozovsky svgroz@outlook.com
 */
class DefaultLockOperationTemplateTest {

    @Test
    void execute() {
        LockStore lockStore = new LockStore() {

            private int att = 0;

            @Override
            public boolean acquire(LockData lockData) {
                if (att == 3) {
                    return true;
                }

                att = att + 1;
                return false;
            }

            @Override
            public boolean delete(LockData lockData) {
                return true;
            }
        };

        var template = new DefaultLockOperationTemplate(lockStore);

        ExecutionResult<Integer> res = template.execute(
                new LockData(
                        "test",
                        UUID.randomUUID().toString(),
                        ZonedDateTime.now(),
                        Duration.ofSeconds(10),
                        Duration.ofMillis(100),
                        Duration.ofMinutes(5)

                ),
                () -> 10
        );
        Assertions.assertNotNull(res);
        Assertions.assertTrue(res.isExecuted());
        Assertions.assertNull(res.getLockingException());
        Assertions.assertNotNull(res.getResult());

    }
}
