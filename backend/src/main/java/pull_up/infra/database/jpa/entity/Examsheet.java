package pull_up.infra.database.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import pull_up.infra.database.jpa.embedded.Problemsheet;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Examsheet(String examTitle, Map<Integer, Long> problemMap) {
        this.examTitle = examTitle;
        this.problemsheets = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : problemMap.entrySet()) {
            problemsheets.add(new Problemsheet(entry.getKey(), entry.getValue()));
        }
        this.examCount = 0;
        this.averageScore = 0.0;
        this.averageDuration = Duration.ZERO;
    }

    public static Examsheet create(String examTitle, Map<Integer, Long> problemMap) {
        return new Examsheet(examTitle, problemMap);
    }

    public Map<Integer, Long> getProblemMap() {
        Map<Integer, Long> problemMap = new HashMap<>();
        for (Problemsheet problemsheet : problemsheets) {
            problemMap.put(problemsheet.getProblemNumber(), problemsheet.getProblemId());
        }
        return problemMap;
    }

    public void mark(Integer score, Duration duration) {
        Double totalScore = averageScore * examCount;
        Duration totalDuration = averageDuration.multipliedBy(examCount);

        this.examCount++;
        this.averageScore = (totalScore + score) / examCount;
        this.averageDuration = totalDuration.plus(duration).dividedBy(examCount);
    }
}
