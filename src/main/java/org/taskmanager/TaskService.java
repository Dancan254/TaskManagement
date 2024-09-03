package org.taskmanager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    //method to map the resultSet to the task for querying
    public Task mapResultSetToTask(ResultSet resultSet) throws SQLException{

        Task task = new Task();
        task.setId(resultSet.getLong("id"));
        task.setTitle(resultSet.getString("title"));
        task.setDescription(resultSet.getString("description"));
        task.setStatus(Task.Status.valueOf(resultSet.getString("status")));
        task.setCreated_date(resultSet.getTimestamp("created_date").toLocalDateTime());
        task.setDueDate(resultSet.getTimestamp("due_date") != null ? resultSet.getTimestamp("due_date").toLocalDateTime() : null);
        return task;
    }

    //get all the tasks
    public List<Task>  getAllTasks(){
        String query = "SELECT * FROM tasks";
        List<Task> tasks = new ArrayList<>();

        try(Connection connection = DataBaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet set = statement.executeQuery()){
            while(set.next()){
                Task task = mapResultSetToTask(set);
                tasks.add(task);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return tasks;
    }

    public List<Task> getTaskByStatus(Task.Status status){
        return getAllTasks().stream().filter(task -> task.getStatus() == status).
                collect(Collectors.toList());
    }

    public List<Task> getTaskDueSoon(){
        return getAllTasks().stream().filter(task -> task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now().plusDays(3)))
                .collect(Collectors.toList());
    }
}
