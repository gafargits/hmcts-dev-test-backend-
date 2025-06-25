package uk.gov.hmcts.reform.dev.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskRequest {
    private String status;
    private String description;
    private String title;
    private LocalDate dueDate;
    private Integer caseworkerId;
}
