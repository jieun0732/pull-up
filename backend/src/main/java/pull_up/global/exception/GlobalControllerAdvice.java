package pull_up.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pull_up.domain.exam.exception.ExamException;
import pull_up.global.dto.MessageDto;
import pull_up.domain.member.member.IncorrectAnswerException;
import pull_up.domain.member.member.AnswerException;
import pull_up.domain.member.member.MemberException;
import pull_up.domain.problem.problem.ProblemException;


/**
 * 전체 Exception Handler.
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Member Exception Handler.
     */
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<?> applicationHandler(MemberException e) {
        log.error("Member Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new MessageDto(e.getMessage()));
    }

    /**
     * MemberAnswer Exception Handler.
     */
    @ExceptionHandler(AnswerException.class)
    public ResponseEntity<?> applicationHandler(AnswerException e) {
        log.error("MemberAnswer Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new MessageDto(e.getMessage()));
    }

    /**
     * IncorrectAnswer Exception Handler.
     */
    @ExceptionHandler(IncorrectAnswerException.class)
    public ResponseEntity<?> applicationHandler(IncorrectAnswerException e) {
        log.error("IncorrectAnswer Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new MessageDto(e.getMessage()));
    }

    /**
     * Problem Exception Handler.
     */
    @ExceptionHandler(ProblemException.class)
    public ResponseEntity<?> applicationHandler(ProblemException e) {
        log.error("Problem Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new MessageDto(e.getMessage()));
    }

    /**
     * Exam Exception Handler.
     */
    @ExceptionHandler(ExamException.class)
    public ResponseEntity<?> applicationHandler(ExamException e) {
        log.error("Exam Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new MessageDto(e.getMessage()));
    }

}
