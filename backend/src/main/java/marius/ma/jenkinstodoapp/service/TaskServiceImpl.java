package marius.ma.jenkinstodoapp.service;

import marius.ma.jenkinstodoapp.dto.TaskDto;
import marius.ma.jenkinstodoapp.mapper.TaskMapper;
import marius.ma.jenkinstodoapp.model.Task;
import marius.ma.jenkinstodoapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElse(null);
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Override
    public TaskDto updateTask(Long id, TaskDto taskDetailsDto) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setTitle(taskDetailsDto.title());
            task.setDescription(taskDetailsDto.description());
            task.setDone(taskDetailsDto.done());
            Task updatedTask = taskRepository.save(task);
            return taskMapper.toDto(updatedTask);
        }
        return null;
    }

    @Override
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
