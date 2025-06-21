package uk.gov.hmcts.reform.dev.enums;

public enum TaskStatus {
    TODO,
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    BLOCKED;

    public static TaskStatus findStatus(String status) {
        for(TaskStatus s : TaskStatus.values()) {
            if(s.name().equalsIgnoreCase(status)) return s;
        }
        throw new IllegalArgumentException("Invalid task status");
    }
}
