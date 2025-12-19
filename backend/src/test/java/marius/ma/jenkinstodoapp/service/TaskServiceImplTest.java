package marius.ma.jenkinstodoapp.service;

import marius.ma.jenkinstodoapp.dto.TaskDto;
import marius.ma.jenkinstodoapp.mapper.TaskMapper;
import marius.ma.jenkinstodoapp.model.Task;
import marius.ma.jenkinstodoapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        task = new Task("Test Task", "Test Description", false);
        task.setId(1L);
        taskDto = new TaskDto(1L, "Test Task", "Test Description", false);
    }

    @Test
    void getAllTasks() {
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        List<TaskDto> tasks = taskService.getAllTasks();

        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(1);
        assertThat(tasks.get(0).title()).isEqualTo("Test Task");
        verify(taskRepository).findAll();
    }

    @Test
    void getTaskById_shouldReturnTask_whenExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto foundTask = taskService.getTaskById(1L);

        assertThat(foundTask).isNotNull();
        assertThat(foundTask.id()).isEqualTo(1L);
        verify(taskRepository).findById(1L);
    }

    @Test
    void getTaskById_shouldReturnNull_whenNotExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        TaskDto foundTask = taskService.getTaskById(1L);

        assertThat(foundTask).isNull();
        verify(taskRepository).findById(1L);
    }

    @Test
    void createTask() {
        when(taskMapper.toEntity(any(TaskDto.class))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDto(any(Task.class))).thenReturn(taskDto);

        TaskDto createdTask = taskService.createTask(taskDto);

        assertThat(createdTask).isNotNull();
        assertThat(createdTask.title()).isEqualTo("Test Task");
        verify(taskRepository).save(task);
    }

    @Test
    void updateTask_shouldUpdateTask_whenExists() {
        TaskDto updatedDetails = new TaskDto(1L, "Updated Task", "Updated Description", true);
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setDone(true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(updatedDetails);

        TaskDto result = taskService.updateTask(1L, updatedDetails);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Updated Task");
        assertThat(result.done()).isTrue();
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
    }

    @Test
    void updateTask_shouldReturnNull_whenNotExists() {
        TaskDto updatedDetails = new TaskDto(1L, "Updated Task", "Updated Description", true);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        TaskDto result = taskService.updateTask(1L, updatedDetails);

        assertThat(result).isNull();
        verify(taskRepository).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_shouldReturnTrue_whenDeleted() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        boolean isDeleted = taskService.deleteTask(1L);

        assertThat(isDeleted).isTrue();
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTask_shouldReturnFalse_whenNotExists() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        boolean isDeleted = taskService.deleteTask(1L);

        assertThat(isDeleted).isFalse();
        verify(taskRepository, never()).deleteById(1L);
    }
}
