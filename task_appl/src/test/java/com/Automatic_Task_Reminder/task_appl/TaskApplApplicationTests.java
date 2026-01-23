package com.Automatic_Task_Reminder.task_appl;

import com.Automatic_Task_Reminder.task_appl.Entity.User;
import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.Repository.TaskRepository;
import com.Automatic_Task_Reminder.task_appl.Service.TaskService;
import com.Automatic_Task_Reminder.task_appl.enums.PriorityEnum;
import com.Automatic_Task_Reminder.task_appl.enums.StatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskApplApplicationTests {

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskService taskService;

	private final Integer mockUserId = 1;

	@Test
	void testAddTask() {
		taskModel task = new taskModel();
		task.setId(1);
		User user = new User();
		user.setId(mockUserId);
		task.setUser(user);

		when(taskRepository.save(task)).thenReturn(task);

		taskModel returnTask = taskService.addTask(task);
		assertEquals(1, returnTask.getId());
		assertEquals(mockUserId, returnTask.getUser().getId());
		verify(taskRepository, times(1)).save(task);
	}

	@Test
	void testDeleteTaskByUser() {
		long taskId = 1;
		User user = new User();
		user.setId(mockUserId);

		taskModel task = new taskModel();
		task.setId(taskId);
		task.setUser(user);

		when(taskRepository.findByIdAndUserId(taskId, mockUserId)).thenReturn(task);
		doNothing().when(taskRepository).delete(task);

		taskService.deleteTaskByUser(taskId, mockUserId);
		verify(taskRepository, times(1)).delete(task);
	}

	@Test
	void testFindTaskByUser() {
		long taskId = 11;
		taskModel task = new taskModel();
		task.setTitle("hello");
		User user = new User();
		user.setId(mockUserId);
		task.setUser(user);

		when(taskRepository.findByIdAndUserId(taskId, mockUserId)).thenReturn(task);

		taskModel returnTask = taskService.findTaskByUser(taskId, mockUserId);
		assertEquals("hello", returnTask.getTitle());
		assertEquals(mockUserId, returnTask.getUser().getId());
		verify(taskRepository, times(1)).findByIdAndUserId(taskId, mockUserId);
	}

	@Test
	void testUpdateTaskByUser() {
		long taskId = 11;
		User user = new User();
		user.setId(mockUserId);

		taskModel existingTask = new taskModel();
		existingTask.setId(taskId);
		existingTask.setTitle("learn sql and java");
		existingTask.setStatus(StatusEnum.IN_PROGRESS);
		existingTask.setPriority(PriorityEnum.LOW);
		existingTask.setUser(user);

		taskModel updatedTask = new taskModel();
		updatedTask.setTitle("New Title");
		updatedTask.setDescription("New Desc");
		updatedTask.setStatus(StatusEnum.IN_PROGRESS);
		updatedTask.setPriority(PriorityEnum.HIGH);
		updatedTask.setDueDate(LocalDate.now().plusDays(3));
		updatedTask.setUser(user);

		when(taskRepository.findByIdAndUserId(taskId, mockUserId)).thenReturn(existingTask);
		when(taskRepository.save(any(taskModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

		taskModel result = taskService.updateTaskByUser(taskId, updatedTask, mockUserId);

		assertNotNull(result);
		assertEquals(updatedTask.getTitle(), result.getTitle());
		assertEquals(updatedTask.getDescription(), result.getDescription());
		assertEquals(updatedTask.getStatus(), result.getStatus());
		assertEquals(updatedTask.getPriority(), result.getPriority());
		assertEquals(updatedTask.getDueDate(), result.getDueDate());
		assertEquals(mockUserId, result.getUser().getId());

		verify(taskRepository, times(1)).save(existingTask);
		verify(taskRepository, times(1)).findByIdAndUserId(taskId, mockUserId);
	}

	@Test
	void testUpdateMarkAsDoneByUser() {
		long taskId = 5;
		User user = new User();
		user.setId(mockUserId);

		taskModel task = new taskModel();
		task.setId(taskId);
		task.setStatus(StatusEnum.IN_PROGRESS);
		task.setUser(user);

		when(taskRepository.findByIdAndUserId(taskId, mockUserId)).thenReturn(task);
		when(taskRepository.save(any(taskModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

		taskModel result = taskService.updateMarkAsDoneByUser(taskId, mockUserId);
		assertEquals(StatusEnum.DONE, result.getStatus());
		assertEquals(mockUserId, result.getUser().getId());
		verify(taskRepository, times(1)).save(task);
	}

	@Test
	void testUpdateCompletedAtByUser() {
		long taskId = 5;
		User user = new User();
		user.setId(mockUserId);

		taskModel task = new taskModel();
		task.setId(taskId);
		task.setUser(user);

		when(taskRepository.findByIdAndUserId(taskId, mockUserId)).thenReturn(task);
		when(taskRepository.save(any(taskModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

		taskModel result = taskService.updateCompletedAtByUser(taskId, mockUserId);
		assertNotNull(result.getCompletedAt());
		assertEquals(mockUserId, result.getUser().getId());
		verify(taskRepository, times(1)).save(task);
	}

}
