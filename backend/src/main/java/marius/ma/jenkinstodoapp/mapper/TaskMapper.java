package marius.ma.jenkinstodoapp.mapper;

import marius.ma.jenkinstodoapp.dto.TaskDto;
import marius.ma.jenkinstodoapp.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDto toDto(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isDone()
        );
    }

    public Task toEntity(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }
        Task task = new Task();
        task.setId(taskDto.id());
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setDone(taskDto.done());
        return task;
    }
}
