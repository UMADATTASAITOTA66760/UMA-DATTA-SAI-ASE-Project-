package com.arjuncodes.studentsystem.repository;


import com.arjuncodes.studentsystem.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
}

