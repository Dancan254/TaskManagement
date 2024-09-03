package org.taskmanager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final TaskService taskService = new TaskService();

    public static void main(String[] args) {
        Scanner scanner  = new Scanner(System.in);
        boolean running = true;
        System.out.println("Welcome to the Task Management Application!");

        while(running){
            System.out.println("\nChoose an option:");
            System.out.println("1. Add a task");
            System.out.println("2. View all tasks");
            System.out.println("3. View tasks by status");
            System.out.println("4. View tasks due soon");
            System.out.println("5. Update a task");
            System.out.println("6. Delete a task");
            System.out.println("7. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice){
                case 1 -> addTask(scanner);
                case 2 -> viewAllTasks();
                case 3 -> viewTasksByStatus(scanner);
                case 7 -> {
                    running = false;
                    System.out.println("Exiting the app. Goodbye!");
                }
                default -> System.out.println("Invalid choice try again");
            }

        }
        scanner.close();
    }

    private static void addTask(Scanner scanner) {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        System.out.print("Enter task due date (YYYY-MM-DDTHH:MM:SS): ");
        LocalDateTime dueDate = LocalDateTime.parse(scanner.nextLine());

        // Automatically set the created date and default status
        Task task = new Task(null, title, description, Task.Status.PENDING, LocalDateTime.now(), dueDate);
        taskService.createTask(task);
    }
    private static void viewAllTasks(){
        List<Task> tasks = taskService.getAllTasks();
        tasks.forEach(System.out::println);
    }
    private static void viewTasksByStatus(Scanner scanner) {
        System.out.print("Enter task status (PENDING, IN_PROGRESS, COMPLETED): ");
        String input = scanner.nextLine().toUpperCase();
        List<Task> tasks = taskService.getTaskByStatus(input);
        tasks.forEach(System.out::println);
    }
}