package com.multithreading;

import lombok.extern.slf4j.Slf4j;

public class Interrupt {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new BlockingTask());

        thread.start();

        //then do not need interrupt handling
//        thread.setDaemon(true);

        Thread.sleep(100);

        thread.join(200);

        thread.interrupt();

    }

    @Slf4j
    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            log.info(String.valueOf(calculate()));
        }

        private int calculate() {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    log.error("interrupted");
                    return 0;
                }
            }
            return Integer.MAX_VALUE;
        }

    }

}
