package com.Automatic_Task_Reminder.task_appl.Service;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.Repository.TaskRepository;
import com.Automatic_Task_Reminder.task_appl.enums.PriorityEnum;
import com.Automatic_Task_Reminder.task_appl.enums.StatusEnum;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<taskModel> getTasks(int pageNo, int pageSize, String sortBy, StatusEnum status, PriorityEnum priority, String filter) {
        System.out.println(filter);
        Sort sort =Sort.by(sortBy).ascending();

        if (status != null  || priority != null  || filter != null && !filter.isEmpty()){
            List<taskModel> filters = null;
            if (status != null  && priority != null && filter != null && !filter.isEmpty()) {
                filters = taskRepository.findByStatusAndPriorityAndTitleContaining(status, priority, filter);
            } else if (status == null && priority != null && filter != null && !filter.isEmpty()) {
                filters = taskRepository.findByPriorityAndTitleContaining(priority, filter);
            } else if (status != null  && priority == null && filter != null && !filter.isEmpty() ){
                filters = taskRepository.findByStatusAndTitleContaining(status, filter);
            } else if ( status != null && priority != null  && (filter==null || filter.isEmpty())) {
                filters = taskRepository.findByStatusAndPriority(status, priority);
            } else if (status != null ) {
                filters = taskRepository.findByStatus(status);
            } else if (priority != null ) {
                filters = taskRepository.findByPriority(priority);
            } else {
                filters = taskRepository.findByTitleContaining(filter);
            }

            return filters.stream()
                    .sorted((a, b) -> a.getTitle().compareTo(b.getTitle()))
                    .skip((long) pageNo * pageSize)
                    .limit(pageSize)
                    .toList();
        }


        return taskRepository.findAll(PageRequest.of(pageNo,pageSize,sort)).getContent();
    }

    public void addTask(taskModel taskModel1) {
        taskRepository.save(taskModel1);
    }

    public void deleteTask(long id) {
        taskRepository.deleteById(id);
    }

    public taskModel findTask(long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public void updateTask(long id, taskModel updatedTask) {
        taskRepository.findById(id).ifPresent(existing -> {
            existing.setTitle(updatedTask.getTitle());
            existing.setDescription(updatedTask.getDescription());
            existing.setStatus(updatedTask.getStatus());
            existing.setPriority(updatedTask.getPriority());
            existing.setDueDate(updatedTask.getDueDate());
            taskRepository.save(existing);
        });
    }

    public void updateMarkAsDone(long id) {
        Optional<taskModel> task=taskRepository.findById(id);
        if(task.isPresent()){
           taskModel model=task.get();
           model.setStatus(StatusEnum.DONE);
           taskRepository.save(model);
        }
    }
    public long getTotalCount(StatusEnum status, PriorityEnum priority, String filter) {

        if (status != null
                || priority != null
                || filter != null && !filter.isEmpty()) {

            if (status != null
                    && priority != null
                    && filter != null && !filter.isEmpty()) {
                return taskRepository.countByStatusAndPriorityAndTitleContaining(status, priority, filter);
            } else if (status != null
                    && priority != null) {
                return taskRepository.countByStatusAndPriority(status, priority);
            } else if (status != null ){
                return taskRepository.countByStatus(status);
            } else if (priority != null ) {
                return taskRepository.countByPriority(priority);
            } else {
                return taskRepository.countByTitleContaining(filter);
            }
        }

        return taskRepository.count();
    }

    public void updateCompletedAt(long id) {
        taskModel task=taskRepository.findById(id).orElse(null);
        task.setCompletedAt(LocalDateTime.now());
        taskRepository.save(task);
    }
}
