package com.safedjio.multitask.reader;

import com.safedjio.multitask.entity.User;
import com.safedjio.multitask.exception.ServerException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataReader {
    public List<User> loadUsers(String filePath) throws ServerException {
        List<User> users = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(line -> {
                String[] data = line.trim().split("\\s+");
                if (data.length == 2) {
                    users.add(new User(Integer.parseInt(data[0]), Integer.parseInt(data[1])));
                }
            });
        } catch (IOException | NumberFormatException e) {
            throw new ServerException("Error reading input file: " + filePath, e);
        }
        return users;
    }
}