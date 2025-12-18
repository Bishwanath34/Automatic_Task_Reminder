package com.Automatic_Task_Reminder.task_appl.Repository;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.enums.PriorityEnum;
import com.Automatic_Task_Reminder.task_appl.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface TaskRepository extends JpaRepository<taskModel,Long> {
//    List<taskModel> findByTitle(String keyword);
List<taskModel> findByStatusAndPriorityAndTitleContainingIgnoreCase(
        StatusEnum status,
        PriorityEnum priority,
        String title
);

    List<taskModel> findByStatusAndTitleContainingIgnoreCase(StatusEnum status, String title);

    List<taskModel> findByPriorityAndTitleContainingIgnoreCase(PriorityEnum priority, String title);

    List<taskModel> findByStatusAndPriority(StatusEnum status, PriorityEnum priority);

    List<taskModel> findByStatus(StatusEnum status);

    List<taskModel> findByPriority(PriorityEnum priority);

    List<taskModel> findByTitleContainingIgnoreCase(String title);
   long  countByStatusAndPriorityAndTitleContainingIgnoreCase(StatusEnum status,PriorityEnum  priority,String filter);
    long countByStatusAndPriority(StatusEnum status,PriorityEnum priority);
long countByStatus(StatusEnum status);
    long countByPriority(PriorityEnum priority);
   long  countByTitleContainingIgnoreCase(String filter);
}
