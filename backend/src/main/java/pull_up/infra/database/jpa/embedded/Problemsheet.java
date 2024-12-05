package pull_up.infra.database.jpa.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pull_up.infra.database.jpa.entity.Examsheet;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Problemsheet {

    @Column(nullable = false)
    private Integer problemNumber;

    @Column(nullable = false)
    private Long problemId;

    public Problemsheet(Integer problemNumber, Long problemId) {
        this.problemNumber = problemNumber;
        this.problemId = problemId;
    }

    public static Map<Integer, Problem> getProblemSheetMap(List<Problem> problemList, Examsheet examSheet) {
        Map<Integer, Problem> ret = new HashMap<>();
        for (Map.Entry<Integer, Long> entry : examSheet.getProblemMap().entrySet()) {
            for (Problem problem : problemList) {
                if (Objects.equals(problem.getId(), entry.getValue())) {
                    ret.put(entry.getKey(), problem);
                    break;
                }
            }
        }
        return ret;
    }

    public void changeProblem(Long newId) {
        this.problemId = newId;
    }

    public static Problemsheet getEmpty(Integer integer) {
        return new Problemsheet(integer, -1L);
    }
}