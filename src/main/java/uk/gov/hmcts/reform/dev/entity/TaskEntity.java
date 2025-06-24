package uk.gov.hmcts.reform.dev.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "task", schema = "public", catalog = "postgres")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "title")
    @NotEmpty
    private String title;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "status")
    @NotEmpty
    private String status;
    @Basic
    @Column(name = "due_date")
    private LocalDate dueDate;
    @Basic
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @ManyToOne
    @JoinColumn(name = "caseworker_id")
    private CaseworkerEntity caseworker;
}
