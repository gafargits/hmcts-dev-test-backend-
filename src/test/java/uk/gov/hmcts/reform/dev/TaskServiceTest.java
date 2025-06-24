package uk.gov.hmcts.reform.dev;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.dev.entity.CaseworkerEntity;
import uk.gov.hmcts.reform.dev.entity.TaskEntity;
import uk.gov.hmcts.reform.dev.models.CreateTaskRequest;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.models.UpdateTaskRequest;
import uk.gov.hmcts.reform.dev.repository.CaseworkerRepository;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;
import uk.gov.hmcts.reform.dev.service.CaseworkerService;
import uk.gov.hmcts.reform.dev.service.TaskService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;
    @InjectMocks
    private CaseworkerService caseworkerService;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private CaseworkerRepository caseworkerRepository;
    private CreateTaskRequest request;
    private Task task;
    private TaskEntity taskEntity;
    private CaseworkerEntity caseworkerEntity = new CaseworkerEntity();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskRepository = mock(TaskRepository.class);
        caseworkerRepository = mock(CaseworkerRepository.class);
        taskService = new TaskService(taskRepository, caseworkerRepository);
        request = new CreateTaskRequest("Title", "Description", "TODO", LocalDate.now().plusDays(1), 1);
        task = new Task(1, request.getTitle(), request.getDescription(), request.getStatus(), "Bob", request.getDueDate(),  LocalDateTime.now() );
        caseworkerEntity.setId(1);
        caseworkerEntity.setName("Bob");
        taskEntity = new TaskEntity(1, request.getTitle(), request.getDescription(),  request.getStatus(),  request.getDueDate(), LocalDateTime.now(), caseworkerEntity);
    }

    @Test
    void shouldCreateTaskWithValidData() {
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);
        when(caseworkerRepository.findById(anyInt())).thenReturn(Optional.of(caseworkerEntity));

        Task task = taskService.createTask(request);
        assertNotNull(task.getId());
        assertEquals("TODO", task.getStatus());
    }

    @Test
    void shouldThrowWhenTitleIsMissing() {
        assertThrows(IllegalArgumentException.class, () ->
            taskService.createTask(new CreateTaskRequest("", "Desc", "TODO", LocalDate.now().plusDays(1), 1))
        );
    }

    @Test
    void shouldThrowWhenStatusIsInvalid() {
        assertThrows(IllegalArgumentException.class, () ->
            taskService.createTask( new CreateTaskRequest("Valid Title", "Desc", "INVALID_STATUS", LocalDate.now().plusDays(1), 1))
        );
    }

    @Test
    void shouldThrowWhenDueDateIsInThePast() {
        assertThrows(IllegalArgumentException.class, () ->
            taskService.createTask(new CreateTaskRequest("Title", "Desc", "TODO", LocalDate.now().minusDays(1), 1))
        );
    }

    @Test
    void shouldUpdateTaskTitle() {
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);
        when(taskRepository.findById(taskEntity.getId())).thenReturn(Optional.of(taskEntity));
        when(caseworkerRepository.findById(anyInt())).thenReturn(Optional.of(caseworkerEntity));

        Task task = taskService.createTask(new CreateTaskRequest("Old Title", "Desc", "TODO", LocalDate.now().plusDays(1), 1));
        taskService.updateTaskStatus(task.getId(), new UpdateTaskRequest("TODO", "Desc", "New Title", LocalDate.now().plusDays(1), 1));
        Task updated = taskService.findTaskById(task.getId());
        assertEquals("New Title", updated.getTitle());
    }

    @Test
    void shouldReturnTaskById() {
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);
        when(taskRepository.findById(taskEntity.getId())).thenReturn(Optional.of(taskEntity));
        when(caseworkerRepository.findById(anyInt())).thenReturn(Optional.of(caseworkerEntity));

        Task task = taskService.createTask(new CreateTaskRequest("Title", "Desc", "TODO", LocalDate.now().plusDays(1), 1));
        Task found = taskService.findTaskById(task.getId());

        assertEquals(task.getId(), found.getId());
    }

    @Test
    void shouldThrowIfTaskIdDoesNotExist() {
        assertThrows(EntityNotFoundException.class, () -> taskService.findTaskById(999));
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);
        when(caseworkerRepository.findById(anyInt())).thenReturn(Optional.of(caseworkerEntity));

        Task task = taskService.createTask(new CreateTaskRequest("Title", "Desc", "TODO", LocalDate.now().plusDays(1), 1));
        taskService.deleteTask(task.getId());
        assertThrows(EntityNotFoundException.class, () -> taskService.findTaskById(task.getId()));
    }

    @Test
    void shouldListAllTasks() {
        TaskEntity taskEntity2 = new TaskEntity(2, request.getTitle()+"2", request.getDescription()+"2", request.getStatus(), request.getDueDate(), LocalDateTime.now(), caseworkerEntity);
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);
        when(taskRepository.findAll()).thenReturn(List.of(taskEntity, taskEntity2));
        when(caseworkerRepository.findById(anyInt())).thenReturn(Optional.of(caseworkerEntity));

        taskService.createTask(new CreateTaskRequest("Task 1", "Desc", "TODO", LocalDate.now().plusDays(1), 1));
        taskService.createTask(new CreateTaskRequest("Task 2", "Desc", "PENDING", LocalDate.now().plusDays(2), 1));
        List<Task> tasks = taskService.findAllTasks();
        assertTrue(tasks.size() >= 2);
    }

    @Test
    void shouldUpdateTaskStatus() {
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);
        when(taskRepository.findById(taskEntity.getId())).thenReturn(Optional.of(taskEntity));
        when(caseworkerRepository.findById(anyInt())).thenReturn(Optional.of(caseworkerEntity));

        Task task = taskService.createTask(new CreateTaskRequest("Task", "Desc", "TODO", LocalDate.now().plusDays(1), 1));
        taskService.updateTaskStatus(task.getId(), new UpdateTaskRequest("COMPLETED", "Desc", "Task", LocalDate.now().plusDays(1), 1));
        Task updated = taskService.findTaskById(task.getId());
        assertEquals("COMPLETED", updated.getStatus());
    }
}
