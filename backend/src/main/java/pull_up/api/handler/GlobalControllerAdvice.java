package pull_up.api.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pull_up.domain.exam.exception.ExamException;
import pull_up.api.dto.MessageDto;
import pull_up.domain.answer.exception.AnswerException;
import pull_up.domain.member.exception.MemberException;
import pull_up.domain.problem.exception.ProblemException;


@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<?> applicationHandler(MemberException e) {
        log.error("Member Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(AnswerException.class)
    public ResponseEntity<?> applicationHandler(AnswerException e) {
        log.error("MemberAnswer Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(ProblemException.class)
    public ResponseEntity<?> applicationHandler(ProblemException e) {
        log.error("Problem Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(ExamException.class)
    public ResponseEntity<?> applicationHandler(ExamException e) {
        log.error("Exam Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new MessageDto(e.getMessage()));
    }

}
