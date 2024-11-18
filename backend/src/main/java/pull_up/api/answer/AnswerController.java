package pull_up.api.answer;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.api.answer.dto.AnswerDto;
import pull_up.api.answer.dto.AnswerSolved;
import pull_up.api.answer.dto.AnswerSubmit;
import pull_up.domain.deprecated.AnswerServiceL;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerServiceL answerServiceL;

    @Operation(summary = "문제 답안 제출하기", description = "해당 문제의 답안을 제출합니다.", tags = "Deprecated")
    @PostMapping("/submit")
    public ResponseEntity<AnswerSubmit.Response> submit(@RequestBody AnswerSubmit.Request request) {
        return ResponseEntity.ok(answerServiceL.submit(request));
    }

    @Operation(summary = "푼 문제 상세 조회하기", description = "Id로 풀었던 문제를 상세 조회합니다.", tags = "Deprecated")
    @GetMapping("/solved/{id}")
    public ResponseEntity<AnswerDto> getSolved(@PathVariable Long id) {
        return ResponseEntity.ok(answerServiceL.getSolved(id));
    }

    @Operation(summary = "영역별 푼 문제 조회하기(전체)", description = "사용자 id로 영역별 풀었던 문제 전체를 조회합니다.", tags = "Deprecated")
    @GetMapping("/solved/entry/{entry}")
    public ResponseEntity<AnswerSolved> getSolvedAll(
            @PathVariable String entry,
            @RequestParam Long memberId) {
        return ResponseEntity.ok(answerServiceL.getSolvedAll(memberId, entry));
    }
}
