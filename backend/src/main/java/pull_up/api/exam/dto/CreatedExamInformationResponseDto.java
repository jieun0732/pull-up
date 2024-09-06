package pull_up.api.exam.dto;

import java.io.Serializable;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.member.dto.MemberDto;
/**
 * DTO for creating a new {@link ExamInformation} with only the necessary fields.
 */
public record CreatedExamInformationResponseDto(MemberDto member, String entry, String category, String type) implements Serializable {

    // 팩토리 메서드 for 간편 생성
    public static CreatedExamInformationResponseDto of(MemberDto member, String entry, String category, String type) {
        return new CreatedExamInformationResponseDto(member, entry, category, type);
    }

    // Entity로부터 DTO를 생성하는 메서드
    public static CreatedExamInformationResponseDto from(ExamInformation entity) {
        return new CreatedExamInformationResponseDto(
            MemberDto.from(entity.getMember()),
            entity.getEntry(),
            entity.getCategory(),
            entity.getType()
        );
    }

    // DTO로부터 Entity를 생성하는 메서드
    public static ExamInformation toEntity(CreatedExamInformationResponseDto dto) {
        return ExamInformation.of(
            MemberDto.toEntity(dto.member()),
            dto.entry(),
            dto.category(),
            dto.type(),
            null, // createdDate는 Entity에서 설정
            null, // solvedDate는 나중에 설정
            null, // requiredTime도 나중에 설정
            0     // 초기 점수
        );
    }
}