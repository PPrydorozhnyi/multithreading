package com.multithreading.vault;

import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VaultTest {

    static final int MAX_PASSWORD = 9999;
    public static void main(String[] args) {

        Vault vault = new Vault(new Random().nextInt(MAX_PASSWORD));
        List<Thread> threads = List.of(new AscendingHackerThread(vault), new DescendingHackerThread(vault),
                new PoliceThread());

        threads.forEach(Thread::start);
    }

    @Slf4j
    @Data
    @AllArgsConstructor
    private static class Vault {
        private final int password;

        public boolean isCorrectPassword(int guessedPassword) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                log.error("InterruptedException in isCorrectPassword", e);
            }
            return password == guessedPassword;
        }
    }

    @Slf4j
    private abstract static class HackerThread extends Thread {

        protected Vault vault;

        public HackerThread(Vault vault) {
            this.vault = vault;
            setName(getClass().getSimpleName());
            setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public synchronized void start() {
            log.info("Starting thread " + getName());
            super.start();
        }

    }

    private static class AscendingHackerThread extends HackerThread {

        public AscendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int i = 0; i < MAX_PASSWORD; i++) {
                if (vault.isCorrectPassword(i)) {
                    log.info(getName() + " guessed password " + i);
                    System.exit(0);
                }
            }
        }

    }

    private static class DescendingHackerThread extends HackerThread {

        public DescendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int i = MAX_PASSWORD - 1; i  >= 0; i--) {
                if (vault.isCorrectPassword(i)) {
                    log.info(getName() + " guessed password " + i);
                    System.exit(0);
                }
            }
        }

    }

    private static class PoliceThread extends Thread {

        @Override
        public void run() {
            for (int i = 10; i > 0; i--) {
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    log.error("InterruptedException in police count", e);
                }
                log.info("Count is {}", i);
            }
            log.info("Game over");
            System.exit(0);
        }

    }

}
