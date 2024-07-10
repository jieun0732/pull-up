package pull_up.api.exam.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import pull_up.api.exam.entity.ExamProblem;
import pull_up.api.problem.dto.ProblemDto;

/**
 * DTO for {@link ExamProblem}
 */
public record ExamProblemDto(Long id, ExamInformationDto examInformation, ProblemDto problem,
                             LocalDateTime deletedAt) implements
    Serializable {

    public static ExamProblemDto of(Long id, ExamInformationDto examInformation, ProblemDto problem,
        LocalDateTime deletedAt) {
        return new ExamProblemDto(id, examInformation, problem, deletedAt);
    }

    public static ExamProblemDto from(ExamProblem entity) {
        return new ExamProblemDto(entity.getId(),
            ExamInformationDto.from(entity.getExamInformation(), problemDtos),
            ProblemDto.from(entity.getProblem()), entity.getDeletedAt());
    }

    public static ExamProblem toEntity(ExamProblemDto dto) {
        return ExamProblem.of(ExamInformationDto.toEntity(dto.examInformation()),
            ProblemDto.toEntity(dto.problem()));
    }
}