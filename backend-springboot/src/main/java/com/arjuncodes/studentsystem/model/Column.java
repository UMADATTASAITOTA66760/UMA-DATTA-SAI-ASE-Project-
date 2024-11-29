package com.arjuncodes.studentsystem.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "columns")
public class Column {

    @Id
    private String id;
    private String name;  // e.g., To Do, In Progress, Done

    public Column(String name) {
        this.name = name;
    }

    // Getters and Setters
}

