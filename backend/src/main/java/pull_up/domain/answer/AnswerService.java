package pull_up.domain.answer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.answer.dto.CreateAnswer;
import pull_up.api.exam.exception.ExamErrorCode;
import pull_up.api.exam.exception.ExamException;
import pull_up.infra.database.entity.Answer;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.repository.exam.ExamRepository;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final ExamRepository examRepository;

    public CreateAnswer.Response submit(CreateAnswer.Request request) {
        Exam exam = examRepository.findByIdWithAnswer(request.examId())
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));

        Answer answer = Answer.submit(exam, request.problemNumber(), request.selectedAnswer());

        return CreateAnswer.Response.toDto(answer);
    }
}
