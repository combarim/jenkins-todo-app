package marius.ma.jenkinstodoapp.controller;

import marius.ma.jenkinstodoapp.dto.TaskDto;
import marius.ma.jenkinstodoapp.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDto> getAll() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto task) {
        TaskDto saved = taskService.createTask(task);
        return ResponseEntity.created(URI.create("/tasks/" + saved.id())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> update(@PathVariable Long id, @RequestBody TaskDto task) {
        TaskDto updated = taskService.updateTask(id, task);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
