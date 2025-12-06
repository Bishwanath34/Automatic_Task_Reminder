package com.Automatic_Task_Reminder.task_appl.Controller;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.Service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api")
public class controller {
    @Autowired
    private TaskService taskService;
       @GetMapping("/tasks")
    public String getTasks(Model model){
           model.addAttribute("tasks",taskService.getTasks());
return "index";
       }
      @GetMapping("/")
    public String  viewForm(Model model){
           model.addAttribute("task",new taskModel());
           return "add";
      }
      @PostMapping("/task/create")
      public String addTask(@Valid @ModelAttribute("task") taskModel taskModel, BindingResult bindingResult){
           if(bindingResult.hasErrors()){
               return "add";
           }
           taskModel.setCreatedAt(LocalDateTime.now());
           taskService.addTask(taskModel);
           return "redirect:/api/task/success";
      }
      @GetMapping("/task/success")
      public String success(){
           return "success";
      }
@GetMapping("/task/{id}")
    public String deleteTask(@PathVariable long id){
           taskService.deleteTask(id);
           return "redirect:/api/tasks";
}
@GetMapping("/form/{id}")
    public String updateForm(@PathVariable long id,Model model){
    taskModel task=taskService.findTask(id);
           model.addAttribute("update1",task);
           return "update";
}
@PostMapping("/task/update")
    public String updateTask(@Valid @ModelAttribute("update1") taskModel taskModel1,BindingResult bindingResult){
           if(bindingResult.hasErrors()){
               return "update";
           }
           taskService.updateTask(taskModel1);
           return "redirect:/api/tasks";
}

}
