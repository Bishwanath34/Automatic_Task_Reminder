package com.Automatic_Task_Reminder.task_appl.Controller;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.Service.TaskService;
import com.Automatic_Task_Reminder.task_appl.enums.PriorityEnum;
import com.Automatic_Task_Reminder.task_appl.enums.StatusEnum;
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
public class taskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public String getTasks(
            Model model,
            @RequestParam(required = false, defaultValue = "0") int pageNo,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false) StatusEnum status,
            @RequestParam(required = false) PriorityEnum priority,
            @RequestParam(required = false) String keyword) {
List<taskModel> tasks=taskService.getTasks(pageNo, pageSize, sortBy,status,priority,keyword);

long totalItems=taskService.getTotalCount(status,priority,keyword);
int totalPages=(int) Math.ceil((double)totalItems/pageSize);
        model.addAttribute("tasks",tasks);
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("pageSize",pageSize);

        return "index";
    }

    @GetMapping("/")
    public String viewForm(Model model) {
        model.addAttribute("task", new taskModel());
        model.addAttribute("statuses",StatusEnum.values());
        model.addAttribute("priorities",PriorityEnum.values());

        return "add";
    }

    @PostMapping("/task/create")
    public String addTask(
            @Valid @ModelAttribute("task") taskModel taskModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "add";
        }

        taskModel.setCreatedAt(LocalDateTime.now());
        taskService.addTask(taskModel);
        return "redirect:/api/task/success";
    }

    @GetMapping("/task/success")
    public String success() {
        return "success";
    }

    @GetMapping("/task/{id}")
    public String deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
        return "redirect:/api/tasks?pageNo=0&pageSize=5";
    }

    @GetMapping("/form/{id}")
    public String updateForm(@PathVariable long id, Model model) {
        taskModel task = taskService.findTask(id);
        model.addAttribute("update1", task);
        model.addAttribute("statuses",StatusEnum.values());
        model.addAttribute("priorities",PriorityEnum.values());
        return "update";
    }

    @PostMapping("/task/update")
    public String updateTask(
            @Valid @ModelAttribute("update1") taskModel taskModel1,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "update";
        }

        taskService.updateTask(taskModel1.getId(), taskModel1);
        return "redirect:/api/tasks?pageNo=0&pageSize=5";
    }
    @GetMapping("/taskUpdate/{id}")
    public String updateMarkAsDone(@PathVariable long id){
        taskService.updateMarkAsDone(id);
        taskService.updateCompletedAt(id);
        return "redirect:/api/tasks?pageNo=0&pageSize=5";
    }
@GetMapping("/view/{id}")
    public String view(@PathVariable long id,Model model){
taskModel task=taskService.findTask(id);
model.addAttribute("view1",task);
return "view";
}
}
