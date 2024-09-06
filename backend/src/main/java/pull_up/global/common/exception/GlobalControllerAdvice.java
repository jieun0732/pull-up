package pull_up.global.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pull_up.api.exam.exception.ExamException;
import pull_up.api.member.exception.IncorrectAnswerErrorCode;
import pull_up.api.member.exception.IncorrectAnswerException;
import pull_up.api.member.exception.MemberAnswerException;
import pull_up.api.member.exception.MemberException;
import pull_up.api.problem.exception.ProblemException;
import pull_up.global.common.response.BaseResponse;


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
            .body(BaseResponse.error(e.getErrorCode().getHttpStatus().value(), e.getMessage()));
    }

    /**
     * MemberAnswer Exception Handler.
     */
    @ExceptionHandler(MemberAnswerException.class)
    public ResponseEntity<?> applicationHandler(MemberAnswerException e) {
        log.error("MemberAnswer Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(BaseResponse.error(e.getErrorCode().getHttpStatus().value(), e.getMessage()));
    }

    /**
     * IncorrectAnswer Exception Handler.
     */
    @ExceptionHandler(IncorrectAnswerException.class)
    public ResponseEntity<?> applicationHandler(IncorrectAnswerException e) {
        log.error("IncorrectAnswer Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(BaseResponse.error(e.getErrorCode().getHttpStatus().value(), e.getMessage()));
    }

    /**
     * Problem Exception Handler.
     */
    @ExceptionHandler(ProblemException.class)
    public ResponseEntity<?> applicationHandler(ProblemException e) {
        log.error("Problem Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(BaseResponse.error(e.getErrorCode().getHttpStatus().value(), e.getMessage()));
    }

    /**
     * Exam Exception Handler.
     */
    @ExceptionHandler(ExamException.class)
    public ResponseEntity<?> applicationHandler(ExamException e) {
        log.error("Exam Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(BaseResponse.error(e.getErrorCode().getHttpStatus().value(), e.getMessage()));
    }

}
