package pull_up.api.exam.dto;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import pull_up.api.problem.dto.ProblemTypeResultDto;

public record ExamInformationDetailDto(
    Long examId,
    LocalDateTime createdDate,
    LocalDateTime solvedDate,
    Duration requiredTime,
    Integer score,
    List<ProblemTypeResultDto> problemTypeResults,
    Double averageScore,
    Duration averageTime,
    Integer totalCorrectAnswers,
    String rankPercent
) implements Serializable {

    public static ExamInformationDetailDto of(
        Long examId,
        LocalDateTime createdDate,
        LocalDateTime solvedDate,
        Duration requiredTime,
        Integer score,
        List<ProblemTypeResultDto> problemTypeResults,
        Double averageScore,
        Duration averageTime,
        Integer totalCorrectAnswers,
        String rankPercent
    ) {
        return new ExamInformationDetailDto(
            examId,
            createdDate,
            solvedDate,
            requiredTime,
            score,
            problemTypeResults,
            averageScore,
            averageTime,
            totalCorrectAnswers,
            rankPercent
        );
    }
}