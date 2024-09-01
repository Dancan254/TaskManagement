package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskService {
    public void createTask(Task task) {
        if (taskExists(task.getTitle())) {
            System.out.println("Task already exists");
            return;
        }
        String query = "INSERT INTO tasks (title, description, status, created_date, due_date) VALUES (?, ?, ?, ?, ?)";

        try(Connection connection = DataBaseConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getStatus().name());
            preparedStatement.setObject(4, task.getCreated_date());
            preparedStatement.setObject(5, task.getDueDate());
            preparedStatement.executeUpdate();

            System.out.println("Task created successfully");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create task", e);
        }
    }

    public boolean taskExists(String title) {
        String query = "SELECT COUNT(*) FROM tasks WHERE LOWER(title) = LOWER(?)";
        try (Connection connection = DataBaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check task existence", e);
        }
        return false;
    }
}
