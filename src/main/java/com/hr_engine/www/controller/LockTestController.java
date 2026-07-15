package com.hr_engine.www.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hr_engine.www.service.BookingLockService;

@RestController
@RequestMapping("/api/test-lock")
public class LockTestController {

    @Autowired
    private BookingLockService bookingLockService;
    
    @Autowired
    private RedissonClient redissonClient;

    @PostMapping("/{roomId}")
    public String tryLock(@PathVariable Long roomId) {
        boolean acquired = bookingLockService.tryLockRoom(roomId);
        return acquired ? "Lock acquired for room " + roomId
                        : "Room " + roomId + " is currently locked by someone else";
    }

    @DeleteMapping("/{roomId}")
    public String unlock(@PathVariable Long roomId) {
        RLock lock = redissonClient.getLock("lock:room:" + roomId);
        boolean unlocked = lock.forceUnlock();
        return unlocked ? "Lock forcibly released for room " + roomId
                         : "No lock existed for room " + roomId;
    }

    @GetMapping("/{roomId}")
    public String checkLock(@PathVariable Long roomId) {
        return "Room " + roomId + " locked: " + bookingLockService.isRoomLocked(roomId);
    }
    
    
}