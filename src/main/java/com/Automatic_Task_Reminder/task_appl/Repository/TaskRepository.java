package com.Automatic_Task_Reminder.task_appl.Repository;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.enums.PriorityEnum;
import com.Automatic_Task_Reminder.task_appl.enums.StatusEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<taskModel, Long> {

    // Single task by ID and user
    taskModel findByIdAndUserId(Long id, Integer userId);

    // List tasks for user
    List<taskModel> findByUserId(Integer userId);
    List<taskModel> findByUserId(Integer userId, Pageable pageable);

    // Filters
    List<taskModel> findByUserIdAndStatus(Integer userId, StatusEnum status);
    List<taskModel> findByUserIdAndPriority(Integer userId, PriorityEnum priority);
    List<taskModel> findByUserIdAndTitleContainingIgnoreCase(Integer userId, String title);
    List<taskModel> findByUserIdAndStatusAndPriority(Integer userId, StatusEnum status, PriorityEnum priority);
    List<taskModel> findByUserIdAndStatusAndTitleContainingIgnoreCase(Integer userId, StatusEnum status, String title);
    List<taskModel> findByUserIdAndPriorityAndTitleContainingIgnoreCase(Integer userId, PriorityEnum priority, String title);
    List<taskModel> findByUserIdAndStatusAndPriorityAndTitleContainingIgnoreCase(Integer userId, StatusEnum status, PriorityEnum priority, String title);

    // Counts
    long countByUserId(Integer userId);
    long countByUserIdAndStatus(Integer userId, StatusEnum status);
    long countByUserIdAndPriority(Integer userId, PriorityEnum priority);
    long countByUserIdAndTitleContainingIgnoreCase(Integer userId, String title);
    long countByUserIdAndStatusAndPriority(Integer userId, StatusEnum status, PriorityEnum priority);
    long countByUserIdAndStatusAndTitleContainingIgnoreCase(Integer userId, StatusEnum status, String title);
    long countByUserIdAndPriorityAndTitleContainingIgnoreCase(Integer userId, PriorityEnum priority, String title);
    long countByUserIdAndStatusAndPriorityAndTitleContainingIgnoreCase(Integer userId, StatusEnum status, PriorityEnum priority, String title);
}
