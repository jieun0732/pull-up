package pull_up.api.exam.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.problem.dto.ProblemDto;

/**
 * DTO for {@link ExamInformation}
 */
public record ExamInformationDto(Long id, MemberDto member, String entry, String category,
                                 String type, LocalDateTime date,
                                 LocalDateTime time, Integer score,
                                 LocalDateTime deletedAt) implements
    Serializable {

    public static ExamInformationDto of(Long id, MemberDto member, String entry, String category,
        String type, LocalDateTime date, LocalDateTime time, Integer score,
        LocalDateTime deletedAt) {
        return new ExamInformationDto(id, member, entry, category, type, date, time, score,
            deletedAt);
    }

    public static ExamInformationDto from(ExamInformation entity, List<ProblemDto> problemDtos) {
        return new ExamInformationDto(entity.getId(), MemberDto.from(entity.getMember()),
            entity.getEntry(),
            entity.getCategory(), entity.getType(), entity.getDate(), entity.getTime(),
            entity.getScore(),
            entity.getDeletedAt());
    }

    public static ExamInformation toEntity(ExamInformationDto dto) {
        return ExamInformation.of(MemberDto.toEntity(dto.member()), dto.entry(), dto.category(),
            dto.type(), dto.date(), dto.time(), dto.score());
    }
}