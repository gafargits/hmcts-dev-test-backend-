package uk.gov.hmcts.reform.dev.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.hmcts.reform.dev.models.CreateTaskRequest;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.models.UpdateTaskRequest;
import uk.gov.hmcts.reform.dev.service.CaseworkerService;
import uk.gov.hmcts.reform.dev.service.TaskService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import(TaskControllerTest.Config.class)
class TaskControllerTest {
    @Autowired
    private transient MockMvc mockMvc;

    @Configuration
    static class Config {
        @Bean
        TaskService taskService() {
            return Mockito.mock(TaskService.class);
        }

        @Bean
        CaseworkerService caseworkerService() {
            return Mockito.mock(CaseworkerService.class);
        }
    }

    @Autowired
    private TaskService taskService;
    @Autowired
    private CaseworkerService caseworkerService;
    @Autowired
    private ObjectMapper objectMapper;
    private Task sampleTask;

    @BeforeEach
    void setup() {
        sampleTask = new Task(1,
                              "Test Task",
                              "Desc",
                              "TODO",
                              "Bob",
                              "1",
                              LocalDate.now().plusDays(1),
                              LocalDateTime.now());
    }

    @DisplayName("Should welcome upon root request with 200 response code")
    @Test
    void welcomeRootEndpoint() throws Exception {
        MvcResult response = mockMvc.perform(get("/")).andExpect(status().isOk()).andReturn();

        assertThat(response.getResponse().getContentAsString()).startsWith("Welcome");
    }

    @Test
    void shouldCreateTask() throws Exception {
        CreateTaskRequest req = new CreateTaskRequest("Test Task", "Desc", "TODO",   LocalDate.now().plusDays(1), 1);
        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(sampleTask);

        mockMvc.perform(post("/task")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(req)))

            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(sampleTask.getId()))
            .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        when(taskService.findTaskById(1)).thenReturn(sampleTask);

        mockMvc.perform(get("/task/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        UpdateTaskRequest updateRequest = new UpdateTaskRequest("COMPLETED",
                                                                "Updated Desc",
                                                                "Updated Title",
                                                                LocalDate.now().plusDays(2),
                                                                1);
        Task updated = new Task(1,
                                "Updated Title",
                                "Updated Desc",
                                "COMPLETED",
                                "Bob",
                                "1",
                                updateRequest.getDueDate(),
                                LocalDateTime.now());

        when(taskService.updateTaskStatus(eq(1), any(UpdateTaskRequest.class))).thenReturn(updated);

        mockMvc.perform(patch("/task/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        when(taskService.deleteTask(1)).thenReturn(true);

        mockMvc.perform(delete("/task/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldListAllTasks() throws Exception {
        when(taskService.findAllTasks()).thenReturn(List.of(sampleTask));

        mockMvc.perform(get("/task/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1));
    }
}
