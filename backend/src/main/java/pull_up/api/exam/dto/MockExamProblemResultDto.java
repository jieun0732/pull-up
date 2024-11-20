package pull_up.api.exam.dto;

import java.io.Serializable;

import pull_up.infra.database.jpa.entity.legacy.AnswerL;

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

    public static MockExamProblemResultDto from(AnswerL entity) {
        boolean isCorrect = entity.getIsCorrect() != null ? entity.getIsCorrect() : false;

        // 각 필드에 대해 null 체크 및 기본값 처리
        String questionText = entity.getProblemL() != null && entity.getProblemL().getQuestion() != null
            ? entity.getProblemL().getQuestion()
            : null;

        String chosenAnswer = entity.getChosenAnswer() != null ? entity.getChosenAnswer() : null;

        String correctAnswer = entity.getProblemL() != null && entity.getProblemL().getAnswer() != null
            ? entity.getProblemL().getAnswer()
            : null;

        return new MockExamProblemResultDto(
            entity.getProblemNumber(),
            isCorrect,
            questionText,
            chosenAnswer,
            correctAnswer
        );
    }
}
