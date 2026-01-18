package com.Automatic_Task_Reminder.task_appl.Controller;

import com.Automatic_Task_Reminder.task_appl.DTO.UserProfileDto;
import com.Automatic_Task_Reminder.task_appl.Entity.User;
import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.Service.TaskService;
import com.Automatic_Task_Reminder.task_appl.Service.UserService;
import com.Automatic_Task_Reminder.task_appl.enums.PriorityEnum;
import com.Automatic_Task_Reminder.task_appl.enums.StatusEnum;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api")
public class taskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    private UserProfileDto setLoggedUserInModel(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return null;
        UserProfileDto dto = userService.mapToProfileDTO(loggedUser);
        model.addAttribute("loggedUser", dto);
        return dto;
    }


    @GetMapping("/tasks")
    public String getTasks(Model model,
                           HttpSession session,
                           @RequestParam(required = false, defaultValue = "0") int pageNo,
                           @RequestParam(required = false, defaultValue = "5") int pageSize,
                           @RequestParam(required = false, defaultValue = "id") String sortBy,
                           @RequestParam(required = false) StatusEnum status,
                           @RequestParam(required = false) PriorityEnum priority,
                           @RequestParam(required = false) String keyword) {

        if (setLoggedUserInModel(session, model) == null) return "redirect:/loginForm";

        User loggedUser = (User) session.getAttribute("loggedUser");

        List<taskModel> tasks = taskService.getTasksByUser(
                loggedUser.getId(), pageNo, pageSize, sortBy, status, priority, keyword
        );
        long totalItems = taskService.getTotalCountByUser(
                loggedUser.getId(), status, priority, keyword
        );
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        model.addAttribute("tasks", tasks);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);

        return "index";
    }

    @GetMapping("/")
    public String viewForm(Model model, HttpSession session) {
        if (setLoggedUserInModel(session, model) == null) return "redirect:/loginForm";

        taskModel task = new taskModel();
        task.setUser((User) session.getAttribute("loggedUser"));
        model.addAttribute("task", task);
        model.addAttribute("statuses", StatusEnum.values());
        model.addAttribute("priorities", PriorityEnum.values());
        return "add";
    }

    @PostMapping("/task/create")
    public String addTask(@Valid @ModelAttribute("task") taskModel taskModel,
                          BindingResult bindingResult,
                          HttpSession session,
                          Model model) {

        if (setLoggedUserInModel(session, model) == null) return "redirect:/loginForm";
        if (bindingResult.hasErrors()) return "add";

        User loggedUser = (User) session.getAttribute("loggedUser");
        taskModel.setUser(loggedUser);
        taskModel.setCreatedAt(LocalDateTime.now());
        taskService.addTask(taskModel);

        return "redirect:/api/tasks?pageNo=0&pageSize=5";
    }

    @GetMapping("/task/{id}")
    public String deleteTask(@PathVariable long id, HttpSession session, Model model) {
        if (setLoggedUserInModel(session, model) == null) return "redirect:/loginForm";

        User loggedUser = (User) session.getAttribute("loggedUser");
        taskService.deleteTaskByUser(id, loggedUser.getId());
        return "redirect:/api/tasks?pageNo=0&pageSize=5";
    }

    // ================= UPDATE TASK =================
    @GetMapping("/form/{id}")
    public String updateForm(@PathVariable long id, Model model, HttpSession session) {
        if (setLoggedUserInModel(session, model) == null) return "redirect:/loginForm";

        User loggedUser = (User) session.getAttribute("loggedUser");
        taskModel task = taskService.findTaskByUser(id, loggedUser.getId());
        if (task == null) return "redirect:/api/tasks?pageNo=0&pageSize=5";

        model.addAttribute("update1", task);
        model.addAttribute("statuses", StatusEnum.values());
        model.addAttribute("priorities", PriorityEnum.values());
        return "update";
    }

    @PostMapping("/task/update")
    public String updateTask(@Valid @ModelAttribute("update1") taskModel taskModel1,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model) {

        if (setLoggedUserInModel(session, model) == null) return "redirect:/loginForm";
        if (bindingResult.hasErrors()) return "update";

        User loggedUser = (User) session.getAttribute("loggedUser");
        taskService.updateTaskByUser(taskModel1.getId(), taskModel1, loggedUser.getId());
        return "redirect:/api/tasks?pageNo=0&pageSize=5";
    }

    @GetMapping("/taskUpdate/{id}")
    public String updateMarkAsDone(@PathVariable long id, HttpSession session, Model model) {
        if (setLoggedUserInModel(session, model) == null) return "redirect:/loginForm";

        User loggedUser = (User) session.getAttribute("loggedUser");
        taskService.updateMarkAsDoneByUser(id, loggedUser.getId());
        taskService.updateCompletedAtByUser(id, loggedUser.getId());
        return "redirect:/api/tasks?pageNo=0&pageSize=5";
    }


    @GetMapping("/view/{id}")
    public String view(@PathVariable long id, Model model, HttpSession session) {
        if (setLoggedUserInModel(session, model) == null) return "redirect:/loginForm";

        User loggedUser = (User) session.getAttribute("loggedUser");
        taskModel task = taskService.findTaskByUser(id, loggedUser.getId());
        if (task == null) return "redirect:/api/tasks?pageNo=0&pageSize=5";

        model.addAttribute("view1", task);
        return "view";
    }

    @GetMapping("/cal")
    public String cal(Model model, HttpSession session) {
        if (setLoggedUserInModel(session, model) == null) return "redirect:/loginForm";

        User loggedUser = (User) session.getAttribute("loggedUser");
        List<taskModel> tasks = taskService.getAllTasksByUser(loggedUser.getId());
        model.addAttribute("tasks", tasks);
        return "calender";
    }

    @GetMapping("/grid")
    public String grid(Model model, HttpSession session,
                       @RequestParam(required = false, defaultValue = "0") int pageNo,
                       @RequestParam(required = false, defaultValue = "6") int pageSize,
                       @RequestParam(required = false, defaultValue = "id") String sortBy) {

        if (setLoggedUserInModel(session, model) == null) return "redirect:/loginForm";

        User loggedUser = (User) session.getAttribute("loggedUser");
        List<taskModel> tasks = taskService.getTasksByUser(
                loggedUser.getId(), pageNo, pageSize, sortBy, null, null, null
        );
        long totalItems = taskService.getTotalCountByUser(loggedUser.getId(), null, null, null);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        model.addAttribute("tasks", tasks);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);

        return "grid";
    }

}
