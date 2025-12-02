package com.Automatic_Task_Reminder.task_appl.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class taskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String description;
    private String category;
    private String priority;
    private String status;
    private String isCompleted;
    private String completedAt;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private LocalDate reminderDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;

    public taskModel() {
    }

    public taskModel(String title, String description, String category,
                     String priority, String status, String isCompleted,
                     String completedAt, LocalDate dueDate, LocalTime dueTime,
                     LocalDate reminderDate, LocalDateTime createdAt,
                     LocalDateTime updatedAt, String createdBy) {

        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.isCompleted = isCompleted;
        this.completedAt = completedAt;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.reminderDate = reminderDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
    }
}
