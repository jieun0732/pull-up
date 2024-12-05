package pull_up.domain.exam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExamErrorCode {
    NOT_FOUND_EXAM(HttpStatus.BAD_REQUEST, "요청한 내용의 시험이 없습니다."),
    NOT_FOUND_EXAM_PROBLEM(HttpStatus.NOT_FOUND, "요청한 내용의 모의고사 문제를 찾을 수 없습니다."),
    PROBLEM_NUMBER_EXCEED(HttpStatus.BAD_REQUEST, "요청한 문제 번호가 시험의 문제 개수 범위를 초과합니다."),
    ALREADY_STARTED_MOCK_EXAM(HttpStatus.BAD_REQUEST, "이미 진행중인 모의고사가 있습니다."),
    NOT_GRADED_MOCK_EXAM(HttpStatus.BAD_REQUEST, "아직 답안지 제출되지 않은 모의고사입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
