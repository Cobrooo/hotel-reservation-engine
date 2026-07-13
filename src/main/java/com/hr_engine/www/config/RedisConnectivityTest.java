package com.hr_engine.www.config;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class RedisConnectivityTest implements CommandLineRunner {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void run(String... args) throws Exception {
        RLock lock = redissonClient.getLock("test:connectivity");
        boolean acquired = lock.tryLock(0, 10, TimeUnit.SECONDS);
        System.out.println("Redis lock test - acquired: " + acquired);
        if (acquired) {
            lock.unlock();
            System.out.println("Redis lock test - released successfully");
        }
    }
    
    
}