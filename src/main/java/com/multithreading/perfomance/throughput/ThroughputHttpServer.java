package com.multithreading.perfomance.throughput;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThroughputHttpServer {

    private static final String INPUT_FILE = "src/main/resources/throughput/war_and_peace.txt";
    private static final int NUMBER_OF_THREADS = 4;


    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));

        startServer(text);
    }

    private static void startServer(String text) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);

        server.createContext("/search", new WordCountHandler(text));
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        server.setExecutor(executor);
        server.start();

        log.info("Server started");
    }

    private static class WordCountHandler implements HttpHandler {

        private final String text;

        public WordCountHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();

            String[] keys = query.split("=");
            String action = keys[0];
            String word = keys[1];

            if (!"word".equals(action)) {
                exchange.sendResponseHeaders(400, 0);
                return;
            }

            long count = countWord(word);

            byte[] response = String.valueOf(count).getBytes();

            exchange.sendResponseHeaders(200, response.length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();
        }

        private long countWord(String word) {
            long count = 0;
            int index = 0;

            while (index >= 0) {
                index = text.indexOf(word, index);

                if (index >= 0) {
                    ++count;
                    ++index;
                }
            }
            log.info("Found {} occurrences in the text", count);
            return count;
        }

    }

}