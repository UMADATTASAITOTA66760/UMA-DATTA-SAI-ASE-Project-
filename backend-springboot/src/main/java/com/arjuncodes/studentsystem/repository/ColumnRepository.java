package com.arjuncodes.studentsystem.repository;
import com.arjuncodes.studentsystem.model.Column;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ColumnRepository extends MongoRepository<Column, String> {
}

