package pull_up.api.member.dto;


import java.io.Serializable;
import pull_up.api.member.entity.MemberAnswer;

/**
 * DTO for MemberAnswer index information
 */
public record MemberAnswerIndexDto(Long id, Long problemId, String chosenAnswer, Boolean isCorrect) implements Serializable {

    public static MemberAnswerIndexDto of(Long id, Long problemId, String chosenAnswer, Boolean isCorrect) {
        return new MemberAnswerIndexDto(id, problemId, chosenAnswer, isCorrect);
    }

    public static MemberAnswerIndexDto from(MemberAnswer entity) {
        return new MemberAnswerIndexDto(
            entity.getId(),
            entity.getProblem() != null ? entity.getProblem().getId() : null,
            entity.getChosenAnswer(),
            entity.getIsCorrect()
        );
    }
}
