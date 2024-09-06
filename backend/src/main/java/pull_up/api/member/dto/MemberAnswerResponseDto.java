package pull_up.api.member.dto;

import java.io.Serializable;
import pull_up.api.member.entity.MemberAnswer;

public record MemberAnswerResponseDto(Long id,
                                      // ID of the MemberAnswer entity
                                      String chosenAnswer
) implements Serializable {

    // 팩토리 메서드 for 간편 생성
    public static MemberAnswerResponseDto of(Long id, String chosenAnswer) {
        return new MemberAnswerResponseDto(id, chosenAnswer);
    }

    // Entity로부터 DTO를 생성하는 메서드
    public static MemberAnswerResponseDto from(MemberAnswer entity) {
        return new MemberAnswerResponseDto(entity.getId(),
            entity.getChosenAnswer());
    }
}