package com.Automatic_Task_Reminder.task_appl.Controller;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/api")
public class controller {
    @Autowired
    private TaskService taskService;
       @GetMapping("/tasks")
    public String getTasks(Model model){
           model.addAttribute("message",taskService.getTasks());
return "index";
       }
      @PutMapping("/task")
    public void addTask(taskModel taskModel1){
           taskService.addTask(taskModel1);
      }



}
