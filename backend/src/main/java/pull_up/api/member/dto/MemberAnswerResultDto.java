package pull_up.api.member.dto;

import java.io.Serializable;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.member.entity.MemberAnswer;
import pull_up.api.problem.dto.ProblemResultDto;

public record MemberAnswerResultDto(Long id, MemberDto member, ProblemResultDto problem,
                                    ExamInformationDto examInformation, String chosenAnswer,
                                    Boolean isCorrect) implements Serializable {

    public static MemberAnswerResultDto of(Long id, MemberDto member, ProblemResultDto problem,
        ExamInformationDto examInformation, String chosenAnswer, Boolean isCorrect) {
        return new MemberAnswerResultDto(id, member, problem, examInformation, chosenAnswer, isCorrect);
    }

    public static MemberAnswerResultDto from(MemberAnswer entity) {
        ExamInformationDto examInformationDto = (entity.getExamInformation() != null) ?
            ExamInformationDto.from(entity.getExamInformation()) : null;

        return new MemberAnswerResultDto(entity.getId(), MemberDto.from(
            entity.getMember()),
            ProblemResultDto.from(entity.getProblem()),
            examInformationDto,
            entity.getChosenAnswer(),
            entity.getIsCorrect());
    }

}

