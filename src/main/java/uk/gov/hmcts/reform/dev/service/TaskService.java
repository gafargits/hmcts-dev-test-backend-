package uk.gov.hmcts.reform.dev.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.entity.TaskEntity;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.models.CreateTaskRequest;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.models.UpdateTaskRequest;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(CreateTaskRequest request) {
        TaskStatus.findStatus(request.getStatus());
        TaskEntity entity = toEntity(request);
        return toTaskModel(taskRepository.save(entity));
    }

    public Task findTaskById(int id) {
        TaskEntity entity = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Task with ID " +id+ " NOT FOUND!"));
        return toTaskModel(entity);
    }

    public List<Task> findAllTasks () {
        return taskRepository.findAll()
            .stream().map(this::toTaskModel)
            .collect(Collectors.toList());
    }

    public Task updateTaskStatus(int id, UpdateTaskRequest updatedTask) {
        TaskStatus.findStatus(updatedTask.getStatus());
        TaskEntity entity = taskRepository.findById(id)
            .map(task -> {
                if (Objects.nonNull(updatedTask.getStatus())) task.setStatus(updatedTask.getStatus());
                if (Objects.nonNull(updatedTask.getDescription())) task.setDescription(updatedTask.getDescription());
                if (Objects.nonNull(updatedTask.getTitle())) task.setTitle(updatedTask.getTitle());
                if(Objects.nonNull(updatedTask.getDueDate())) task.setDueDate(updatedTask.getDueDate());
                taskRepository.saveAndFlush(task);
                return task;
            })
            .orElseThrow(() -> new EntityNotFoundException("Task with ID " + id + " NOT FOUND, Cannot be updated."));
        return toTaskModel(entity);
    }

    public boolean deleteTask(int id) {
        if(taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private TaskEntity toEntity(CreateTaskRequest request) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setStatus(request.getStatus().toUpperCase());
        entity.setDueDate(request.getDueDate());
        entity.setDateCreated(LocalDateTime.now());
        return entity;
    }

    private Task toTaskModel(TaskEntity entity) {
        Task task = new Task();
        task.setDescription(entity.getDescription());
        task.setId(entity.getId());
        task.setTitle(entity.getTitle());
        task.setStatus(entity.getStatus().toUpperCase());
        task.setDueDate(entity.getDueDate());
        task.setDateCreated(entity.getDateCreated());

        return task;
    }

}
