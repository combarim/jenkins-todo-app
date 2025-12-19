package marius.ma.jenkinstodoapp.service;

import marius.ma.jenkinstodoapp.dto.TaskDto;

import java.util.List;

public interface TaskService {
    List<TaskDto> getAllTasks();
    TaskDto getTaskById(Long id);
    TaskDto createTask(TaskDto task);
    TaskDto updateTask(Long id, TaskDto task);
    boolean deleteTask(Long id);
}
