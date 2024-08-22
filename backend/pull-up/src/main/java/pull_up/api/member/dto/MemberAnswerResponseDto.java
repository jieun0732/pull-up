package pull_up.api.member.dto;

import java.io.Serializable;
import pull_up.api.member.entity.MemberAnswer;

public record MemberAnswerResponseDto(Long id,
                                      // ID of the MemberAnswer entity
                                      Long memberId,
                                      // ID of the member who answered
                                      Long problemId,
                                      // ID of the problem answered
                                      Long examInformationId,
                                      // ID of the related ExamInformation
                                      String chosenAnswer
) implements Serializable {

    // 팩토리 메서드 for 간편 생성
    public static MemberAnswerResponseDto of(Long id, Long memberId, Long problemId,
        Long examInformationId, String chosenAnswer) {
        return new MemberAnswerResponseDto(id, memberId, problemId, examInformationId, chosenAnswer);
    }

    // Entity로부터 DTO를 생성하는 메서드
    public static MemberAnswerResponseDto from(MemberAnswer entity) {
        return new MemberAnswerResponseDto(entity.getId(), entity.getMember().getId(),
            entity.getProblem().getId(),
            entity.getExamInformation() != null ? entity.getExamInformation().getId() : null,
            entity.getChosenAnswer());
    }
}