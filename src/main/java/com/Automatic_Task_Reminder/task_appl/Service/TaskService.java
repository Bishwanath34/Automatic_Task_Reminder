package com.Automatic_Task_Reminder.task_appl.Service;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class TaskService {

@Autowired
   private TaskRepository taskRepository;



    public List<taskModel> getTasks() {
        return taskRepository.findAll();
    }

    public void addTask(taskModel taskModel1) {
        taskRepository.save(taskModel1);
    }

    public void deleteTask(long id) {
        taskRepository.deleteById(id);
    }

    public taskModel findTask(long id) {
        Optional<taskModel> task=taskRepository.findById(id);
        if(task.isPresent()) {
            return task.get();
        }else{
            return null;
        }
    }

    public void updateTask(taskModel updatedTask) {
        long id=updatedTask.getId();
        Optional<taskModel> task=taskRepository.findById(id);
        if(task.isPresent()){
            taskModel task1=task.get();
            task1.setTitle(updatedTask.getTitle());
            task1.setDescription(updatedTask.getDescription());
            task1.setStatus(updatedTask.getStatus());
            task1.setPriority(updatedTask.getPriority());
            task1.setDueDate(updatedTask.getDueDate());
        }
    }

}
