package pull_up.api.problem.dto;

import java.io.Serializable;

public record ProblemTypeSummaryDto(
    String category,
    String type, // 문제의 타입
    Long totalProblems, // 해당 타입의 문제 개수
    Long answeredProblems, // 선택된 답변이 있는 문제 개수
    Boolean isCorrect
) implements Serializable {

    public static ProblemTypeSummaryDto of(String category, String type, Long totalProblems, Long answeredProblems, Boolean isCorrect) {
        return new ProblemTypeSummaryDto(category, type, totalProblems, answeredProblems, isCorrect);
    }
}