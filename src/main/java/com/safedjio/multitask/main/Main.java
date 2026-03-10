package com.safedjio.multitask.main;

import com.safedjio.multitask.entity.User;
import com.safedjio.multitask.exception.ServerException;
import com.safedjio.multitask.reader.DataReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        DataReader reader = new DataReader();
        try {
            List<User> users = reader.loadUsers("src/main/resources/users.txt");

            ExecutorService executorService = Executors.newFixedThreadPool(users.size());

            logger.info("Simulation started...");
            List<Future<Integer>> results = executorService.invokeAll(users);

            int totalMemory = 0;

            for (Future<Integer> future : results) {
                totalMemory += future.get();
            }

            executorService.shutdown();
            logger.info("Simulation ended. All users served.Total memory used by users: {}", totalMemory);

        } catch (ServerException | InterruptedException | ExecutionException e) {
            logger.error("Simulation failed!", e);
        }
    }
}

