package com.Automatic_Task_Reminder.task_appl.Repository;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.enums.PriorityEnum;
import com.Automatic_Task_Reminder.task_appl.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface TaskRepository extends JpaRepository<taskModel,Long> {
//    List<taskModel> findByTitle(String keyword);
List<taskModel> findByStatusAndPriorityAndTitleContaining(
        StatusEnum status,
        PriorityEnum priority,
        String title
);

    List<taskModel> findByStatusAndTitleContaining(StatusEnum status, String title);

    List<taskModel> findByPriorityAndTitleContaining(PriorityEnum priority, String title);

    List<taskModel> findByStatusAndPriority(StatusEnum status, PriorityEnum priority);

    List<taskModel> findByStatus(StatusEnum status);

    List<taskModel> findByPriority(PriorityEnum priority);

    List<taskModel> findByTitleContaining(String title);
   int countByStatusAndPriorityAndTitleContaining(StatusEnum status,PriorityEnum  priority,String filter);
    int countByStatusAndPriority(StatusEnum status,PriorityEnum priority);
int countByStatus(StatusEnum status);
    int countByPriority(PriorityEnum priority);
   int  countByTitleContaining(String filter);
}
