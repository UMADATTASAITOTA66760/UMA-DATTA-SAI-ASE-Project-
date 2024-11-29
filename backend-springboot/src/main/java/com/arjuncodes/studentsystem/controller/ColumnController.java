package com.arjuncodes.studentsystem.controller;


import com.arjuncodes.studentsystem.model.Column;
import com.arjuncodes.studentsystem.service.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/columns")
public class ColumnController {

    @Autowired
    private ColumnService columnService;

    @PostMapping("/create")
    public Column createColumn(@RequestBody Column column) {
        return columnService.createColumn(column);
    }

    @GetMapping("/all")
    public List<Column> getAllColumns() {
        return columnService.getAllColumns();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteColumn(@PathVariable String id) {
        columnService.deleteColumn(id);
    }
}
