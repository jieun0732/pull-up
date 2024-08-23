package pull_up.api.exam.dto;

import java.io.Serializable;

public record ExamProblemResponseDto(Long examProblemId,        // ID of the ExamProblem entity
                                     Long problemNumber,
                                     String chosenAnswer        // The answer chosen by the user
) implements Serializable {

    // 팩토리 메서드 for 간편 생성
    public static ExamProblemResponseDto of(Long examProblemId, Long problemNumber, String chosenAnswer) {
        return new ExamProblemResponseDto(examProblemId, problemNumber, chosenAnswer);
    }
}