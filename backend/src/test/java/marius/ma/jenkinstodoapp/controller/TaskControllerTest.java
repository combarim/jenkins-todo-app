package marius.ma.jenkinstodoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import marius.ma.jenkinstodoapp.dto.TaskDto;
import marius.ma.jenkinstodoapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        taskDto = new TaskDto(1L, "Test Task", "Test Description", false);
    }

    @Test
    void getAll() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Collections.singletonList(taskDto));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void create_shouldCreateTask() throws Exception {
        when(taskService.createTask(any(TaskDto.class))).thenReturn(taskDto);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/tasks/1"))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void update_shouldUpdateTask_whenExists() throws Exception {
        TaskDto updatedTaskDto = new TaskDto(1L, "Updated Task", "Updated Description", true);
        when(taskService.updateTask(eq(1L), any(TaskDto.class))).thenReturn(updatedTaskDto);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTaskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void update_shouldReturnNotFound_whenNotExists() throws Exception {
        when(taskService.updateTask(eq(1L), any(TaskDto.class))).thenReturn(null);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldDelete_whenExists() throws Exception {
        when(taskService.deleteTask(1L)).thenReturn(true);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturnNotFound_whenNotExists() throws Exception {
        when(taskService.deleteTask(1L)).thenReturn(false);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNotFound());
    }
}
