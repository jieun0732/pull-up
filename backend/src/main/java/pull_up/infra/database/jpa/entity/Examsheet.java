package pull_up.infra.database.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Duration;
import java.util.List;

@Entity
@Table(name = "examsheet")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Examsheet {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer problemNumber;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @OneToMany(mappedBy = "examsheet", fetch = FetchType.LAZY)
    private List<Exam> exams;
}
