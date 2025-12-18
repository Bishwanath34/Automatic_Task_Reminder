package com.Automatic_Task_Reminder.task_appl.Controller;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.Service.TaskService;
import com.Automatic_Task_Reminder.task_appl.enums.PriorityEnum;
import com.Automatic_Task_Reminder.task_appl.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rest")
public class taskRestController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public List<taskModel> getTasks(
            @RequestParam(required = false, defaultValue = "0") int pageNo,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false) StatusEnum status,
            @RequestParam(required = false) PriorityEnum priority,
            @RequestParam(required = false) String keyword) {

        return taskService.getTasks(pageNo, pageSize, sortBy, status,priority,keyword);
    }

    @PostMapping("/task/create")
    public void addTask(@RequestBody taskModel taskModel) {
        taskService.addTask(taskModel);
    }

    @DeleteMapping("/task/{id}")
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }

    @PutMapping("/task/update/{id}")
    public void updateTask(@PathVariable long id, @RequestBody taskModel taskModel1) {
        taskService.updateTask(id, taskModel1);
    }
}
