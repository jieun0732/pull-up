package pull_up.api.exam.dto;

import java.io.Serializable;
import java.time.Duration;

public record ExamInformationAverageScoreDto(Double averageScore, Duration requiredTime) implements
    Serializable {

    public static ExamInformationAverageScoreDto of(Double averageScore, Duration requiredTime) {
        return new ExamInformationAverageScoreDto(averageScore, requiredTime);
    }

    public static ExamInformationAverageScoreDto from(ExamInformationAverageScoreDto dto) {
        return new ExamInformationAverageScoreDto(dto.averageScore(), dto.requiredTime());
    }
}
