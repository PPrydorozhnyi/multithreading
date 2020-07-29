package com.multithreading;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadByRunnable {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(
                () -> {
                    log.info("In thread {}. Priority {}", Thread.currentThread().getName(),
                            Thread.currentThread().getPriority());
                    throw new IllegalCallerException("Excepty");
                }
        );

        thread.setName("Namerino");

        //setting programmatically priority for OS and epoch
        thread.setPriority(Thread.MAX_PRIORITY);

        thread.setUncaughtExceptionHandler((t, e) -> log.error("Gotcha", e));

        log.info("Currently in thread " + Thread.currentThread().getName() + " before start");
        thread.start();
        log.info("Currently in thread " + Thread.currentThread().getName() + " after start");

        Thread.sleep(10_000);

    }

}
