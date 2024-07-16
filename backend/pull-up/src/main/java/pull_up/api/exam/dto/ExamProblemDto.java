package pull_up.api.exam.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import pull_up.api.exam.entity.ExamProblem;
import pull_up.api.problem.dto.ProblemDto;

/**
 * DTO for {@link ExamProblem}
 */
public record ExamProblemDto(Long id, ExamInformationDto examInformation, ProblemDto problem) implements Serializable {

    public static ExamProblemDto of(Long id, ExamInformationDto examInformation, ProblemDto problem) {
        return new ExamProblemDto(id, examInformation, problem);
    }

    public static ExamProblemDto from(ExamProblem entity) {
        return new ExamProblemDto(entity.getId(),
            ExamInformationDto.from(entity.getExamInformation(), null),
            ProblemDto.from(entity.getProblem()));
    }

    public static ExamProblem toEntity(ExamProblemDto dto) {
        return ExamProblem.of(ExamInformationDto.toEntity(dto.examInformation()),
            ProblemDto.toEntity(dto.problem()));
    }
}