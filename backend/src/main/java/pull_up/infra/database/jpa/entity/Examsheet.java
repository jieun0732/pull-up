package pull_up.infra.database.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import pull_up.global.entity.BaseEntity;

import java.time.Duration;
import java.util.List;

@Entity
@Table(name = "examsheet")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Examsheet extends BaseEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String examTitle;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer examCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Double averageScore;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Duration averageDuration;

    @ElementCollection
    @CollectionTable(name = "problemsheet", joinColumns = @JoinColumn(name = "examsheet_id"))
    private List<Problemsheet> problemsheets;

    @OneToMany(mappedBy = "examsheet", fetch = FetchType.LAZY)
    private List<Exam> exams;
}
