import React, { useState, useEffect, useRef } from 'react';
import {
  Box,
  Paper,
  Typography,
  Button,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Card,
  CardContent,
  Container,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import LogoutIcon from '@mui/icons-material/Logout'

const KanbanBoard = () => {
  const navigate=useNavigate()
  const [columns, setColumns] = useState({
    'todo': { title: 'To Do', items: [] },
    'inProgress': { title: 'In Progress', items: [] },
    'done': { title: 'Done', items: [] },
  });

  const [openNewTask, setOpenNewTask] = useState(false);
  const [newTaskContent, setNewTaskContent] = useState('');
  const [newTaskDescription, setNewTaskDescription] = useState('');
  const dragItem = useRef();
  const userId = localStorage.getItem('userId'); // Assuming userId is stored in local storage

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/tasks/${userId}`);
        const tasks = response.data;
        const taskColumns = {
          todo: { title: 'To Do', items: tasks.filter((task) => task.status === 'Not Started') },
          inProgress: { title: 'In Progress', items: tasks.filter((task) => task.status === 'In-progress') },
          done: { title: 'Done', items: tasks.filter((task) => task.status === 'Completed') },
        };
        setColumns(taskColumns);
      } catch (error) {
        console.error('Error fetching tasks:', error);
      }
    };
    fetchTasks();
  }, [userId]);

  const handleDragStart = (e, columnId, index) => {
    dragItem.current = { columnId, index };
    e.dataTransfer.effectAllowed = 'move';
  };

  const handleDragOver = (e) => {
    e.preventDefault();
    e.dataTransfer.dropEffect = 'move';
  };

  const handleDrop = async (e, targetColumnId) => {
    e.preventDefault();
    if (!dragItem.current) return;

    const { columnId: sourceColumnId, index: sourceIndex } = dragItem.current;
    const movedItem = columns[sourceColumnId].items[sourceIndex];

    if (sourceColumnId === targetColumnId) return;

    const newStatus =
      targetColumnId === 'todo' ? 'Not Started' : targetColumnId === 'inProgress' ? 'In-progress' : 'Completed';

    try {
      const response = await axios.put(`http://localhost:8080/api/tasks/update/${movedItem.id}/${userId}`, {
        ...movedItem,
        status: newStatus,
      });

      const updatedTask = response.data;

      const updatedColumns = { ...columns };

      updatedColumns[sourceColumnId].items.splice(sourceIndex, 1);
      updatedColumns[targetColumnId].items.push(updatedTask);

      setColumns(updatedColumns);
      dragItem.current = null;
    } catch (error) {
      console.error('Error updating task:', error);
    }
  };

  const handleAddTask = async () => {
    if (!newTaskContent.trim() || !newTaskDescription.trim()) return;

    try {
      const response = await axios.post(`http://localhost:8080/api/tasks/create/${userId}`, {
        name: newTaskContent,
        description: newTaskDescription,
        status: 'Not Started',
      });
      const newTask = response.data;

      setColumns((prevColumns) => ({
        ...prevColumns,
        todo: {
          ...prevColumns.todo,
          items: [...prevColumns.todo.items, newTask],
        },
      }));

      setNewTaskContent('');
      setNewTaskDescription('');
      setOpenNewTask(false);
    } catch (error) {
      console.error('Error creating task:', error);
    }
  };

  const handleDeleteTask = async (columnId, taskId, index) => {
    try {
      await axios.delete(`http://localhost:8080/api/tasks/delete/${taskId}/${userId}`);
      const updatedColumns = { ...columns };
      updatedColumns[columnId].items.splice(index, 1);
      setColumns(updatedColumns);
    } catch (error) {
      console.error('Error deleting task:', error);
    }
  };
  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  return (
    <Container maxWidth="lg" sx={{ height: '100vh', py: 3, display: 'flex', justifyContent: 'center' }}>
      <Box sx={{ width: '100%', display: 'flex', justifyContent: 'center', gap: 2, overflowX: 'auto' }}>
        <Box sx={{ display: 'flex', gap: 2, minWidth: 'max-content' }}>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
            <Typography variant="h4" component="h1" sx={{ mb: 3 }}>
              Kanban Board
            </Typography>
            <Button
          variant="contained"
          color="secondary"
          startIcon={<LogoutIcon />}
          onClick={handleLogout}
        >
          Logout
        </Button>
            <Button variant="contained" startIcon={<AddIcon />} onClick={() => setOpenNewTask(true)}>
              Add Task
            </Button>
            <Box sx={{ display: 'flex', gap: 2 }}>
              {Object.entries(columns).map(([columnId, column]) => (
                <Paper
                  key={columnId}
                  sx={{
                    width: 300,
                    flexShrink: 0,
                    bgcolor: 'grey.100',
                    p: 2,
                    display: 'flex',
                    flexDirection: 'column',
                  }}
                >
                  <Typography variant="h6" sx={{ mb: 2 }}>
                    {column.title}
                  </Typography>
                  <Box
                    onDragOver={handleDragOver}
                    onDrop={(e) => handleDrop(e, columnId)}
                    sx={{
                      minHeight: 500,
                      maxHeight: 500,
                      overflowY: 'auto',
                    }}
                  >
                    {column.items.map((item, index) => (
                      <Card
                        key={item.id}
                        draggable
                        onDragStart={(e) => handleDragStart(e, columnId, index)}
                        sx={{ mb: 1, cursor: 'move' }}
                      >
                        <CardContent
                          sx={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            alignItems: 'center',
                            '&:last-child': { pb: 2 },
                          }}
                        >
                          <Typography>
                            {item.name} - {item.description}
                          </Typography>
                          <IconButton
                            size="small"
                            color="error"
                            onClick={() => handleDeleteTask(columnId, item.id, index)}
                          >
                            <DeleteIcon />
                          </IconButton>
                        </CardContent>
                      </Card>
                    ))}
                  </Box>
                </Paper>
              ))}
            </Box>
          </Box>
        </Box>
      </Box>

      <Dialog open={openNewTask} onClose={() => setOpenNewTask(false)}>
        <DialogTitle>Add New Task</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Task Name"
            fullWidth
            value={newTaskContent}
            onChange={(e) => setNewTaskContent(e.target.value)}
          />
          <TextField
            margin="dense"
            label="Task Description"
            fullWidth
            value={newTaskDescription}
            onChange={(e) => setNewTaskDescription(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenNewTask(false)}>Cancel</Button>
          <Button onClick={handleAddTask} variant="contained">
            Add
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default KanbanBoard;
