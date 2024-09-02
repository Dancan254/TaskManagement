package org.taskmanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime created_date;
    private LocalDateTime dueDate;

    public enum Status{
        PENDING,
        IN_PROGRESS,
        DONE
    }
}
