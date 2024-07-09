package pull_up.global.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 전체 Exception Handler.
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

//    /**
//     * Member Exception Handler.
//     */
//    @ExceptionHandler(MemberException.class)
//    public ResponseEntity<?> applicationHandler(MemberException e) {
//        log.error("Member Error occurs {}", e.toString());
//        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
//            .body(BaseResponse.error(e.getErrorCode().getHttpStatus().value(), e.getMessage()));
//    }

}
