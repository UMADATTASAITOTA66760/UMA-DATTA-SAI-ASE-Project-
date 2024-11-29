package com.arjuncodes.studentsystem;

import com.arjuncodes.studentsystem.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class StudentsystemApplicationTests {

    @Autowired
    private TaskService taskService;

    @Test
    void contextLoads() {
        // This checks if the application context loads successfully
    }

    @Test
    void taskServiceIsNotNull() {
        // Test if TaskService bean is correctly injected and not null
        assertNotNull(taskService);
    }
}
