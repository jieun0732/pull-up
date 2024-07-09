package pull_up.api.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 회원관련 ErrorCode.
 */
@Getter
@AllArgsConstructor
public enum MemberErrorCode {

    // 회원가입 에러 코드
    INVALID_FIELD_VALUE(HttpStatus.BAD_REQUEST, "빈 필드값이 있습니다."),
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "중복된 아이디입니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
    PASSWORD_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "비밀번호는 8~20자리이며 특수문자, 영어 대소문자, 숫자를 포함해야 합니다."),
    INVALID_MEMBER_ID(HttpStatus.BAD_REQUEST, "아이디는 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다."),
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NO_AUTHORITY(HttpStatus.BAD_REQUEST, "권한이 없습니다."),
    // 카카오 로그인 에러코드
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 로그인 실패");

    private final HttpStatus httpStatus;
    private final String message;
}
