package uk.gov.hmcts.reform.dev.models;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public class CreateTaskRequest {
    @NotEmpty
    private String title;
    private String description;
    @NotEmpty
    private String status;
    private LocalDate dueDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

}
