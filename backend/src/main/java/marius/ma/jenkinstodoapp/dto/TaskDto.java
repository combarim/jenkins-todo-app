package marius.ma.jenkinstodoapp.dto;

public record TaskDto(
        Long id,
        String title,
        String description,
        boolean done
) {
}
