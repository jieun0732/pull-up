package pull_up.domain.answer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.answer.dto.AnswerDto;
import pull_up.api.answer.dto.AnswerSubmit;
import pull_up.api.exam.exception.ExamErrorCode;
import pull_up.api.exam.exception.ExamException;
import pull_up.global.exception.member.AnswerException;
import pull_up.infra.database.entity.Answer;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.repository.answer.AnswerRepository;
import pull_up.infra.database.repository.exam.ExamRepository;

import static pull_up.global.exception.member.AnswerErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final ExamRepository examRepository;
    private final AnswerRepository answerRepository;

    public AnswerSubmit.Response submit(AnswerSubmit.Request request) {
        Exam exam = examRepository.findByIdWithAnswer(request.examId())
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));

        Answer answer = Answer.submit(exam, request.problemNumber(), request.selectedAnswer());

        return new AnswerSubmit.Response(AnswerDto.toDto(answer));
    }

    public AnswerDto getSolved(Long id) {
        Answer answer = answerRepository.findByIdWithProblem(id, true)
                .orElseThrow(() -> new AnswerException(NOT_FOUND));

        return AnswerDto.toDto(answer);
    }
}
