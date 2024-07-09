package pull_up.api.exam.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.member.dto.MemberDto;

/**
 * DTO for {@link ExamInformation}
 */
public record ExamInformationDto(Long id, MemberDto member, String category, LocalDateTime date,
                                 LocalDateTime time, Integer score,
                                 LocalDateTime deletedAt) implements
    Serializable {

    public static ExamInformationDto of(Long id, MemberDto member, String category, LocalDateTime date, LocalDateTime time, Integer score, LocalDateTime deletedAt) {
        return new ExamInformationDto(id, member, category, date, time, score, deletedAt);
    }

    public static ExamInformationDto from(ExamInformation entity) {
        return new ExamInformationDto(entity.getId(), MemberDto.from(entity.getMember()), entity.getCategory(), entity.getDate(), entity.getTime(), entity.getScore(), entity.getDeletedAt());
    }

    public static ExamInformation toEntity(ExamInformationDto dto) {
        return ExamInformation.of(MemberDto.toEntity(dto.member()), dto.category(), dto.date(), dto.time(), dto.score());
    }
}