package uk.gov.hmcts.reform.dev.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
public class CreateTaskRequest {
    @NotEmpty
    @NotNull(message = "title must not be null")
    private String title;
    private String description;
    @NotEmpty(message = "status must not be empty")
    private String status;
    @NotNull(message = "a task must have a due date")
    private LocalDate dueDate;
    @NotNull(message = "caseworker must not be null")
    private Integer caseworkerId;
}
