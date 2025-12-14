package com.Automatic_Task_Reminder.task_appl.Repository;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface TaskRepository extends JpaRepository<taskModel,Long> {
//    List<taskModel> findByTitle(String keyword);
List<taskModel> findByStatusAndPriorityAndTitleContaining(
        String status,
        String priority,
        String title
);

    List<taskModel> findByStatusAndTitleContaining(String status, String title);

    List<taskModel> findByPriorityAndTitleContaining(String priority, String title);

    List<taskModel> findByStatusAndPriority(String status, String priority);

    List<taskModel> findByStatus(String status);

    List<taskModel> findByPriority(String priority);

    List<taskModel> findByTitleContaining(String title);
   int countByStatusAndPriorityAndTitleContaining(String status,String  priority,String filter);
    int countByStatusAndPriority(String status,String priority);
int countByStatus(String status);
    int countByPriority(String priority);
   int  countByTitleContaining(String filter);
}
