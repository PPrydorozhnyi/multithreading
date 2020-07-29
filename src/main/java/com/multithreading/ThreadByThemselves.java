package com.multithreading;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadByThemselves {

    public static void main(String[] args) {
        Thread thread = new NewThread();

        thread.start();

    }

    private static class NewThread extends Thread {

        @Override
        public void run() {
            log.info("In thread {}. Priority {}", getName(), getPriority());
        }

    }

}
