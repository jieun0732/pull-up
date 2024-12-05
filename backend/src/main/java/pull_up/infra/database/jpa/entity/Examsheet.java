package pull_up.infra.database.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.core.FrameworkListener;
import org.hibernate.annotations.ColumnDefault;
import pull_up.domain.examsheet.exception.ExamsheetErrorCode;
import pull_up.domain.examsheet.exception.ExamsheetException;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.embedded.Problemsheet;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static com.nimbusds.jose.util.StandardCharset.UTF_8;

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

    @Column(nullable = false, unique = true)
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

    public Examsheet(String examTitle, Integer problemCount) {
        this.examTitle = examTitle;
        this.problemsheets = Stream.iterate(1, n -> n + 1).limit(problemCount).map(Problemsheet::getEmpty).toList();
        this.examCount = 0;
        this.averageScore = 0.0;
        this.averageDuration = Duration.ZERO;
    }

    public static Examsheet create(String examTitle, Map<Integer, Long> problemMap) {
        return new Examsheet(examTitle, problemMap);
    }

    public static Examsheet createEmpty(String examTitle, Integer problemCount) {
        return new Examsheet(examTitle, problemCount);
    }

    public Map<Integer, Long> getProblemMap() {
        Map<Integer, Long> problemMap = new HashMap<>();
        for (Problemsheet problemsheet : problemsheets) {
            problemMap.put(problemsheet.getProblemNumber(), problemsheet.getProblemId());
        }
        return problemMap;
    }

    public Map<Integer, Problem> getProblemEntityMap(List<Problem> problems) {
        Map<Integer, Problem> problemMap = new HashMap<>();
        for (Problemsheet problemsheet : problemsheets) {
            if (problemsheet.getProblemId() == -1) {
                problemMap.put(problemsheet.getProblemNumber(),Problem.createEmpty());
                continue;
            }
            for (Problem problem : problems) {
                if (!problem.getId().equals(problemsheet.getProblemId())) continue;
                problemMap.put(problemsheet.getProblemNumber(), problem);
            }
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

    public void modify(Map<String, String> parameters) {
        map(parameters);
    }

    private void map(Map<String, String> parameters) {
        this.examTitle = parameters.get("examTitle");
    }

    public void changeProblem(Integer problemNumber, Long newSelectedId) {
        for (Problemsheet problemsheet : problemsheets) {
            if (problemsheet.getProblemNumber().equals(problemNumber)) {
                problemsheet.changeProblem(newSelectedId);
                updateTime();
                return;
            }
        }
        throw new ExamsheetException(ExamsheetErrorCode.PROBLEM_NUMBER_EXCEED);
    }
}
