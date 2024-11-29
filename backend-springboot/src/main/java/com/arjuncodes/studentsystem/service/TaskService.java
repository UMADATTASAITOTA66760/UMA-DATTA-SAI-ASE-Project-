// package com.arjuncodes.studentsystem.service;

// import com.arjuncodes.studentsystem.model.Task;
// import com.arjuncodes.studentsystem.repository.TaskRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class TaskService {

//     @Autowired
//     private TaskRepository taskRepository;

//     // Create a task for a specific user
//     public Task createTask(Task task, String userId) {
//         task.setUserId(userId); // Assign the task to the logged-in user
//         return taskRepository.save(task);
//     }

//     // Get tasks for a specific user
//     public List<Task> getTasksByUserId(String userId) {
//         return taskRepository.findAll().stream()
//                 .filter(task -> task.getUserId().equals(userId))
//                 .toList();
//     }
// }
package com.arjuncodes.studentsystem.service;

import com.arjuncodes.studentsystem.model.Task;
import com.arjuncodes.studentsystem.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Create a task for a specific user
    public Task createTask(Task task, String userId) {
        task.setUserId(userId); // Assign the task to the logged-in user
        return taskRepository.save(task);
    }

    // Get tasks for a specific user
    public List<Task> getTasksByUserId(String userId) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getUserId().equals(userId))
                .toList();
    }

    // Update a task by ID for a specific user
    public Task updateTask(String taskId, Task updatedTask, String userId) {
        return taskRepository.findById(taskId).map(task -> {
            if (task.getUserId().equals(userId)) { // Ensure the task belongs to the user
                task.setName(updatedTask.getName()); // Use the name from the updated task
                task.setDescription(updatedTask.getDescription()); // Update description
                task.setStatus(updatedTask.getStatus()); // Update status
                return taskRepository.save(task); // Save the updated task
            } else {
                throw new RuntimeException("Unauthorized: Task does not belong to this user.");
            }
            

        }).orElseThrow(() -> new RuntimeException("Task not found."));
    }

    // Delete a task by ID for a specific user
    public void deleteTask(String taskId, String userId) {
        taskRepository.findById(taskId).ifPresent(task -> {
            if (task.getUserId().equals(userId)) { // Ensure the task belongs to the user
                taskRepository.delete(task);
            } else {
                throw new RuntimeException("Unauthorized: Task does not belong to this user.");
            }
        });
    }
}
