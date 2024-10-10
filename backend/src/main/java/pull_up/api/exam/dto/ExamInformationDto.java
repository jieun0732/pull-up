package pull_up.api.exam.dto;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.member.dto.MemberDto;

/**
 * DTO for {@link ExamInformation}
 */
public record ExamInformationDto(Long id, MemberDto member, String entry, String category,
                                 String type, LocalDateTime createdDate,
                                 LocalDateTime solvedDate, Duration requiredTime, Integer score) implements
    Serializable {

    public static ExamInformationDto of(Long id, MemberDto member, String entry, String category,
        String type, LocalDateTime createdDate, LocalDateTime solvedDate, Duration requiredTime, Integer score) {
        return new ExamInformationDto(id, member, entry, category, type, createdDate, solvedDate, requiredTime, score);
    }

    public static ExamInformationDto from(ExamInformation entity) {
        return new ExamInformationDto(entity.getId(), MemberDto.from(entity.getMember()),
            entity.getEntry(),
            entity.getCategory(), entity.getType(), entity.getCreatedDate(), entity.getSolvedDate(),
            entity.getRequiredTime(),
            entity.getScore());
    }

    public static ExamInformation toEntity(ExamInformationDto dto) {
        return ExamInformation.of(MemberDto.toEntity(dto.member()), dto.entry(), dto.category(),
            dto.type(), dto.createdDate(), dto.solvedDate(), dto.requiredTime(), dto.score());
    }
}