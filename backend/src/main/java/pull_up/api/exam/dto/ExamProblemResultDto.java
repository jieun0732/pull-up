package pull_up.api.exam.dto;

import java.io.Serializable;

import pull_up.infra.database.jpa.entity.legacy.AnswerL;
import pull_up.api.problem.dto.ProblemResultDto;

public record ExamProblemResultDto(Long id, ExamInformationDto examInformation,
                                   ProblemResultDto problem, Long problemNumber, String chosenAnswer,
                                   Boolean isCorrect) implements
    Serializable {

    public static ExamProblemResultDto of(Long id, ExamInformationDto examInformation,
        ProblemResultDto problem, Long problemNumber,
        String chosenAnswer, Boolean isCorrect) {
        return new ExamProblemResultDto(id, examInformation, problem, problemNumber, chosenAnswer, isCorrect);
    }

    public static ExamProblemResultDto from(AnswerL entity) {
        return new ExamProblemResultDto(
            entity.getId(),
            ExamInformationDto.from(entity.getExamL()),
            ProblemResultDto.from(entity.getProblemL()),
            entity.getProblemNumber(),
            entity.getChosenAnswer(),
            entity.getIsCorrect()
        );
    }


}