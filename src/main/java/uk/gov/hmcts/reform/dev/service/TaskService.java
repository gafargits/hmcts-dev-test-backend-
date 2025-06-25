package uk.gov.hmcts.reform.dev.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.entity.CaseworkerEntity;
import uk.gov.hmcts.reform.dev.entity.TaskEntity;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.models.CreateTaskRequest;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.models.UpdateTaskRequest;
import uk.gov.hmcts.reform.dev.repository.CaseworkerRepository;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final CaseworkerRepository caseworkerRepository;

    public Task createTask(CreateTaskRequest request) {
        boolean exists = taskRepository.existsByTitleAndCaseworkerId(request.getTitle(), request.getCaseworkerId());
        if (exists) {
            throw new IllegalArgumentException("Task with this title already exists for this caseworker.");
        }
        CaseworkerEntity caseworker = caseworkerRepository.findById(request.getCaseworkerId())
            .orElseThrow(() -> new IllegalArgumentException("Caseworker not found."));

        if (request.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past.");
        }
        TaskStatus.findStatus(request.getStatus()); //validate status
        TaskEntity entity = toEntity(request, caseworker);
        return toTaskModel(taskRepository.save(entity));
    }

    public Task findTaskById(int id) {
        TaskEntity entity = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Task with ID " + id + " NOT FOUND!"));
        return toTaskModel(entity);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll()
            .stream().map(this::toTaskModel)
            .collect(Collectors.toList());
    }

    public Task updateTaskStatus(int id, UpdateTaskRequest updatedTask) {
        TaskStatus.findStatus(updatedTask.getStatus());
        CaseworkerEntity caseworker = caseworkerRepository.findById(updatedTask.getCaseworkerId())
            .orElseThrow(() -> new IllegalArgumentException("Caseworker not found."));
        TaskEntity entity = taskRepository.findById(id)
            .map(task -> {
                if (Objects.nonNull(updatedTask.getStatus())) {
                    task.setStatus(updatedTask.getStatus());
                }
                if (Objects.nonNull(updatedTask.getDescription())) {
                    task.setDescription(updatedTask.getDescription());
                }
                if (Objects.nonNull(updatedTask.getTitle())) {
                    task.setTitle(updatedTask.getTitle());
                }
                if (Objects.nonNull(updatedTask.getDueDate())) {
                    task.setDueDate(updatedTask.getDueDate());
                }
                if (Objects.nonNull(updatedTask.getCaseworkerId())) {
                    task.setCaseworker(caseworker);
                }
                taskRepository.saveAndFlush(task);
                return task;
            })
            .orElseThrow(() -> new EntityNotFoundException("Task with ID " + id + " NOT FOUND, Cannot be updated."));
        return toTaskModel(entity);
    }

    public boolean deleteTask(int id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private TaskEntity toEntity(CreateTaskRequest request, CaseworkerEntity caseworker) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setStatus(request.getStatus().toUpperCase());
        entity.setDueDate(request.getDueDate());
        entity.setDateCreated(LocalDateTime.now());
        entity.setCaseworker(caseworker);
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
        task.setCaseworker(entity.getCaseworker().getName());
        task.setCaseworkerId(entity.getCaseworker().getId().toString());

        return task;
    }

}
