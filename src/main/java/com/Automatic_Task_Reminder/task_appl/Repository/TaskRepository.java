package com.Automatic_Task_Reminder.task_appl.Repository;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<taskModel,Long> {
}
