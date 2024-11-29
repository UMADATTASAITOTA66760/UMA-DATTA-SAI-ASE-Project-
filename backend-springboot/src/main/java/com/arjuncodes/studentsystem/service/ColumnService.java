package com.arjuncodes.studentsystem.service;


import com.arjuncodes.studentsystem.model.Column;
import com.arjuncodes.studentsystem.repository.ColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnService {

    @Autowired
    private ColumnRepository columnRepository;

    public Column createColumn(Column column) {
        return columnRepository.save(column);
    }

    public List<Column> getAllColumns() {
        return columnRepository.findAll();
    }

    public void deleteColumn(String id) {
        columnRepository.deleteById(id);
    }
}
