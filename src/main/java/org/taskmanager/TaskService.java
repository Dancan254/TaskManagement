package org.taskmanager;

import java.sql.*;

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

    //method to update task, needed is the task id
    public void updateTask(Long id, Task updateTask){
        String query = "UPDATE tasks SET title  = ?, description = ?, status = ?, due_date = ?, WHERE id = ?";
        try(Connection connection = DataBaseConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query
            )){
            preparedStatement.setString(1,updateTask.getTitle());
            preparedStatement.setString(2, updateTask.getDescription());
            preparedStatement.setString(3, updateTask.getStatus().name());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(updateTask.getDueDate()));
            preparedStatement.setLong(5, updateTask.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task updated successfully");
            }
            else{
                System.out.println("No task found with the given id");
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to update task", e);

        }
    }
}
