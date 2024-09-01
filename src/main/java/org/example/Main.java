package org.example;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        Task task = new Task();
        task.setTitle("Task 1");
        task.setDescription("Description 1");
        task.setStatus(Task.Status.PENDING);
        task.setCreated_date(LocalDateTime.now());
        task.setDueDate(LocalDateTime.now().plusDays(1));


        TaskService taskService = new TaskService();
        taskService.createTask(task);

    }
}