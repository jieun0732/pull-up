package pull_up.domain.deprecated;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.answer.dto.AnswerDto;
import pull_up.api.answer.dto.AnswerSolved;
import pull_up.api.answer.dto.AnswerSubmit;
import pull_up.api.exam.exception.ExamErrorCode;
import pull_up.api.exam.exception.ExamException;
import pull_up.global.exception.member.AnswerException;
import pull_up.infra.database.entity.legacy.AnswerL;
import pull_up.infra.database.entity.legacy.ExamL;
import pull_up.infra.database.repository.answer.AnswerRepository;
import pull_up.infra.database.repository.exam.ExamRepository;

import java.util.List;

import static pull_up.global.exception.member.AnswerErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final ExamRepository examRepository;
    private final AnswerRepository answerRepository;

    public AnswerSubmit.Response submit(AnswerSubmit.Request request) {
        ExamL examL = examRepository.findByIdWithAnswer(request.examId())
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));

        AnswerL answerL = AnswerL.submit(examL, request.problemNumber(), request.selectedAnswer());

        return new AnswerSubmit.Response(AnswerDto.toDto(answerL));
    }

    public AnswerDto getSolved(Long id) {
        AnswerL answerL = answerRepository.findByIdWithProblem(id, true)
                .orElseThrow(() -> new AnswerException(NOT_FOUND));

        return AnswerDto.toDto(answerL);
    }

    public AnswerSolved getSolvedAll(Long memberId, String entry) {
        List<AnswerL> answerLS = answerRepository.findSolvedAnswersByMemberIdAndEntry(memberId, entry);
        return AnswerSolved.toDto(answerLS);
    }
}
