package pull_up.api.exam.dto;

import java.io.Serializable;
import pull_up.api.exam.entity.ExamProblem;
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

    public static ExamProblemResultDto from(ExamProblem entity) {
        return new ExamProblemResultDto(
            entity.getId(),
            ExamInformationDto.from(entity.getExamInformation()),
            ProblemResultDto.from(entity.getProblem()),
            entity.getProblemNumber(),
            entity.getChosenAnswer(),
            entity.getIsCorrect()
        );
    }


}