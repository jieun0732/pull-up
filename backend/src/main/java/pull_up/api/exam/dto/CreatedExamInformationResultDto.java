package pull_up.api.exam.dto;

import java.io.Serializable;
import java.util.List;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.member.dto.MemberDto;

public record CreatedExamInformationResultDto(Long id, MemberDto member, String entry, String category, String type, List<ExamProblemResultDto> problemIds) implements
    Serializable {

    // 팩토리 메서드 for 간편 생성
    public static CreatedExamInformationResultDto of(Long id, MemberDto member, String entry, String category, String type, List<ExamProblemResultDto> problemIds) {
        return new CreatedExamInformationResultDto(id, member, entry, category, type, problemIds);
    }

    // Entity로부터 DTO를 생성하는 메서드
    public static CreatedExamInformationResultDto from(ExamInformation entity, List<ExamProblemResultDto> problemIds) {
        return new CreatedExamInformationResultDto(
            entity.getId(),
            MemberDto.from(entity.getMember()),
            entity.getEntry(),
            entity.getCategory(),
            entity.getType(),
            problemIds
        );
    }
}