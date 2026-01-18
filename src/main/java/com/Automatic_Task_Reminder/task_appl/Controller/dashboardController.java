package com.Automatic_Task_Reminder.task_appl.Controller;

import com.Automatic_Task_Reminder.task_appl.Entity.User;
import com.Automatic_Task_Reminder.task_appl.Service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class dashboardController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {

        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            return "redirect:/loginForm";
        }

        Integer userId = loggedUser.getId();

        model.addAttribute("pendingCount", taskService.countPending(userId));
        model.addAttribute("completedCount", taskService.countCompleted(userId));
        model.addAttribute("overdueCount", taskService.countOverdue(userId));
        model.addAttribute("totalCount", taskService.countAllTasks(userId));

        model.addAttribute("loggedUser", loggedUser);

        return "dashboard";
    }
}
