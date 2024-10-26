package pull_up.api.exam.dto;

import java.io.Serializable;
import pull_up.api.exam.entity.ExamProblem;

public record MockExamProblemResultDto(Long questionNumber,      // 문제 번호
                                       boolean isCorrect,        // 정답 여부
                                       String questionText,      // 문제 내용
                                       String chosenAnswer,      // 회원이 선택한 답안
                                       String correctAnswer       // 정답) {

) implements Serializable {

    public static MockExamProblemResultDto of(
        Long questionNumber,
        boolean isCorrect,
        String questionText,
        String chosenAnswer,
        String correctAnswer
    ) {
        return new MockExamProblemResultDto(questionNumber, isCorrect, questionText, chosenAnswer,
            correctAnswer);
    }

    public static MockExamProblemResultDto from(ExamProblem entity) {
        return new MockExamProblemResultDto(
            entity.getProblemNumber(),
            entity.getIsCorrect(),
            entity.getProblem().getQuestion(), // 문제 내용
            entity.getChosenAnswer(),
            entity.getProblem().getAnswer() // 정답
        );
    }
}
