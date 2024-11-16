package pull_up.api.exam.dto;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

import pull_up.infra.database.entity.legacy.ExamL;
import pull_up.api.member.dto.MemberDto;

/**
 * DTO for {@link ExamL}
 */
public record ExamInformationDto(Long id, MemberDto member, String entry, String category,
                                 String type, LocalDateTime createdDate,
                                 LocalDateTime solvedDate, Duration requiredTime, Integer score) implements
    Serializable {

    public static ExamInformationDto of(Long id, MemberDto member, String entry, String category,
        String type, LocalDateTime createdDate, LocalDateTime solvedDate, Duration requiredTime, Integer score) {
        return new ExamInformationDto(id, member, entry, category, type, createdDate, solvedDate, requiredTime, score);
    }

    public static ExamInformationDto from(ExamL entity) {
        return new ExamInformationDto(entity.getId(), MemberDto.from(entity.getMemberL()),
            entity.getEntry(),
            entity.getCategory(), entity.getType(), entity.getCreatedDate(), entity.getSolvedTime(),
            entity.getRequiredTime(),
            entity.getScore());
    }

    public static ExamL toEntity(ExamInformationDto dto) {
        return ExamL.of(MemberDto.toEntity(dto.member()), dto.entry(), dto.category(),
            dto.type(), dto.createdDate(), dto.solvedDate(), dto.requiredTime(), dto.score());
    }
}