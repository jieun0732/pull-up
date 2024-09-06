package pull_up.api.member.dto;

import java.io.Serializable;
import pull_up.api.member.entity.MemberAnswer;

public record MemberAnswerSolvedDto(Long problemId, Boolean isCorrect,
                                    String chosenAnswer) implements Serializable {

    public static MemberAnswerSolvedDto from(MemberAnswer memberAnswer) {
        return new MemberAnswerSolvedDto(
            memberAnswer.getProblem().getId(),
            memberAnswer.getIsCorrect(),
            memberAnswer.getChosenAnswer()
        );
    }
}
