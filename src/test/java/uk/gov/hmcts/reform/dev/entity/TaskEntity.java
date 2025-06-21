package uk.gov.hmcts.reform.dev.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "task", schema = "public", catalog = "postgres")
public class TaskEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    @Basic
    @Column(name = "description", nullable = true, length = -1)
    private String description;
    @Basic
    @Column(name = "status", nullable = false)
    private Object status;
    @Basic
    @Column(name = "due_datetime", nullable = true)
    private Timestamp dueDatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Timestamp getDueDatetime() {
        return dueDatetime;
    }

    public void setDueDatetime(Timestamp dueDatetime) {
        this.dueDatetime = dueDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return id == that.id && Objects.equals(title, that.title) && Objects.equals(
            description,
            that.description
        ) && Objects.equals(status, that.status) && Objects.equals(dueDatetime, that.dueDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, dueDatetime);
    }
}
