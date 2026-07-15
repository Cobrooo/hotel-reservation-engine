package com.hr_engine.www.service;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class BookingLockService {

    @Autowired
    private RedissonClient redissonClient;

    @Value("${booking.lock.hold-duration-minutes:5}")
    private long holdDurationMinutes;

    private String buildLockKey(Long roomId) {
        return "lock:room:" + roomId;
    }

    public boolean tryLockRoom(Long roomId) {
        RLock lock = redissonClient.getLock(buildLockKey(roomId));
        try {
            // waitTime = 0: don't wait/retry if already locked, fail immediately
            // leaseTime = holdDurationMinutes: auto-release after this TTL even if never explicitly unlocked
            return lock.tryLock(0, holdDurationMinutes, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public void unlockRoom(Long roomId) {
        RLock lock = redissonClient.getLock(buildLockKey(roomId));
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    public boolean isRoomLocked(Long roomId) {
        RLock lock = redissonClient.getLock(buildLockKey(roomId));
        return lock.isLocked();
    }
    
    // FOR TESTING ONLY — do not use in real booking logic
    public boolean forceUnlockRoom(Long roomId) {
        RLock lock = redissonClient.getLock(buildLockKey(roomId));
        return lock.forceUnlock();
    }
    
}