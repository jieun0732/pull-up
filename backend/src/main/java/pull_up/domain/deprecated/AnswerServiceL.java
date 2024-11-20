package pull_up.domain.deprecated;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.answer.dto.AnswerDto;
import pull_up.api.answer.dto.AnswerSolved;
import pull_up.api.answer.dto.AnswerSubmit;
import pull_up.domain.exam.exception.ExamErrorCode;
import pull_up.domain.exam.exception.ExamException;
import pull_up.domain.answer.exception.AnswerException;
import pull_up.infra.database.jpa.entity.legacy.AnswerL;
import pull_up.infra.database.jpa.entity.legacy.ExamL;
import pull_up.infra.database.jpa.repository.answer.AnswerRepositoryL;
import pull_up.infra.database.jpa.repository.legacy.ExamLRepository;

import java.util.List;

import static pull_up.domain.answer.exception.AnswerErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AnswerServiceL {

    private final ExamLRepository examRepositoryL;
    private final AnswerRepositoryL answerRepositoryL;

    public AnswerSubmit.Response submit(AnswerSubmit.Request request) {
        ExamL examL = examRepositoryL.findByIdWithAnswer(request.examId())
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));

        AnswerL answerL = AnswerL.submit(examL, request.problemNumber(), request.selectedAnswer());

        return new AnswerSubmit.Response(AnswerDto.toDto(answerL));
    }

    public AnswerDto getSolved(Long id) {
        AnswerL answerL = answerRepositoryL.findByIdWithProblem(id, true)
                .orElseThrow(() -> new AnswerException(NOT_FOUND));

        return AnswerDto.toDto(answerL);
    }

    public AnswerSolved getSolvedAll(Long memberId, String entry) {
        List<AnswerL> answerLS = answerRepositoryL.findSolvedAnswersByMemberIdAndEntry(memberId, entry);
        return AnswerSolved.toDto(answerLS);
    }
}
