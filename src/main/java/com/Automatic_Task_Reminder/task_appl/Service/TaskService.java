package com.Automatic_Task_Reminder.task_appl.Service;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.Repository.TaskRepository;
import com.Automatic_Task_Reminder.task_appl.enums.PriorityEnum;
import com.Automatic_Task_Reminder.task_appl.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Get tasks with pagination and filters
    public List<taskModel> getTasksByUser(Integer userId, int pageNo, int pageSize, String sortBy, StatusEnum status, PriorityEnum priority, String filter) {
        Sort sort = Sort.by(sortBy).ascending();

        if (status != null || priority != null || (filter != null && !filter.isEmpty())) {
            List<taskModel> filters = null;
            if (status != null && priority != null && filter != null && !filter.isEmpty()) {
                filters = taskRepository.findByUserIdAndStatusAndPriorityAndTitleContainingIgnoreCase(userId, status, priority, filter);
            } else if (status == null && priority != null && filter != null && !filter.isEmpty()) {
                filters = taskRepository.findByUserIdAndPriorityAndTitleContainingIgnoreCase(userId, priority, filter);
            } else if (status != null && priority == null && filter != null && !filter.isEmpty()) {
                filters = taskRepository.findByUserIdAndStatusAndTitleContainingIgnoreCase(userId, status, filter);
            } else if (status != null && priority != null) {
                filters = taskRepository.findByUserIdAndStatusAndPriority(userId, status, priority);
            } else if (status != null) {
                filters = taskRepository.findByUserIdAndStatus(userId, status);
            } else if (priority != null) {
                filters = taskRepository.findByUserIdAndPriority(userId, priority);
            } else {
                filters = taskRepository.findByUserIdAndTitleContainingIgnoreCase(userId, filter);
            }

            return filters.stream()
                    .skip((long) pageNo * pageSize)
                    .limit(pageSize)
                    .toList();
        }

        return taskRepository.findByUserId(userId, PageRequest.of(pageNo, pageSize, sort));
    }

    // Total count for pagination
    public long getTotalCountByUser(Integer userId, StatusEnum status, PriorityEnum priority, String filter) {
        if (status != null || priority != null || (filter != null && !filter.isEmpty())) {
            if (status != null && priority != null && filter != null && !filter.isEmpty()) {
                return taskRepository.countByUserIdAndStatusAndPriorityAndTitleContainingIgnoreCase(userId, status, priority, filter);
            } else if (status != null && priority != null) {
                return taskRepository.countByUserIdAndStatusAndPriority(userId, status, priority);
            } else if (status != null) {
                return taskRepository.countByUserIdAndStatus(userId, status);
            } else if (priority != null) {
                return taskRepository.countByUserIdAndPriority(userId, priority);
            } else {
                return taskRepository.countByUserIdAndTitleContainingIgnoreCase(userId, filter);
            }
        }
        return taskRepository.countByUserId(userId);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public taskModel addTask(taskModel taskModel) {
        return taskRepository.save(taskModel);
    }

    @Caching(evict = {
            @CacheEvict(value = "task", key = "#id"),
            @CacheEvict(value = "tasks", allEntries = true)
    })
    public void deleteTaskByUser(long id, Integer userId) {
        taskModel task = taskRepository.findByIdAndUserId(id, userId);
        if (task != null) taskRepository.delete(task);
    }

    @Cacheable(value = "task", key = "#id")
    public taskModel findTaskByUser(long id, Integer userId) {
        return taskRepository.findByIdAndUserId(id, userId);
    }

    @CachePut(value = "task", key = "#id")
    @CacheEvict(value = "tasks", allEntries = true)
    public taskModel updateTaskByUser(long id, taskModel updatedTask, Integer userId) {
        taskModel existing = taskRepository.findByIdAndUserId(id, userId);
        if (existing == null) return null;

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setStatus(updatedTask.getStatus());
        existing.setPriority(updatedTask.getPriority());
        existing.setDueDate(updatedTask.getDueDate());
        return taskRepository.save(existing);
    }

    @CachePut(value = "task", key = "#id")
    @CacheEvict(value = "tasks", allEntries = true)
    public taskModel updateMarkAsDoneByUser(long id, Integer userId) {
        taskModel task = taskRepository.findByIdAndUserId(id, userId);
        if (task == null) return null;

        task.setStatus(StatusEnum.DONE);
        return taskRepository.save(task);
    }

    @CachePut(value = "task", key = "#id")
    @CacheEvict(value = "tasks", allEntries = true)
    public taskModel updateCompletedAtByUser(long id, Integer userId) {
        taskModel task = taskRepository.findByIdAndUserId(id, userId);
        if (task == null) return null;

        task.setCompletedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public List<taskModel> getAllTasksByUser(Integer userId) {
        return taskRepository.findByUserId(userId);
    }
}
