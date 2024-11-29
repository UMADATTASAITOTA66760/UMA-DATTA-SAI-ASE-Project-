
package com.arjuncodes.studentsystem.controller;

import com.arjuncodes.studentsystem.model.Task;
import com.arjuncodes.studentsystem.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Create task for a specific user
    @PostMapping("/create/{userId}")
    public Task createTask(@RequestBody Task task, @PathVariable String userId) {
        return taskService.createTask(task, userId);
    }

    // Get all tasks for a specific user
    @GetMapping("/{userId}")
    public List<Task> getUserTasks(@PathVariable String userId) {
        return taskService.getTasksByUserId(userId);
    }

    // Update a task by ID for a specific user
    @PutMapping("/update/{taskId}/{userId}")
    public Task updateTask(
            @PathVariable String taskId,
            @PathVariable String userId,
            @RequestBody Task updatedTask) {
        return taskService.updateTask(taskId, updatedTask, userId);
    }

    // Delete a task by ID for a specific user
    @DeleteMapping("/delete/{taskId}/{userId}")
    public void deleteTask(@PathVariable String taskId, @PathVariable String userId) {
        taskService.deleteTask(taskId, userId);
    }
}
