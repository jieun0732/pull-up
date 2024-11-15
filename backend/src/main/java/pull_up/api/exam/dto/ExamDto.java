package pull_up.api.exam.dto;

import pull_up.api.member.dto.MemberDto;

import java.time.Duration;
import java.time.LocalDateTime;

public record ExamDto(
        Long id,
        String entry,
        String category,
        String type,
        LocalDateTime createdDate,
        LocalDateTime solvedDate,
        Duration requiredTime,
        Integer score,
        Boolean isSolved
) {
}
