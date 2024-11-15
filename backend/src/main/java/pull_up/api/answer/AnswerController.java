package pull_up.api.answer;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.api.answer.dto.AnswerDto;
import pull_up.api.answer.dto.Submit;
import pull_up.domain.answer.AnswerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerService answerService;

    @Operation(summary = "문제 답안 제출하기", description = "해당 문제의 답안을 제출합니다.", tags = "풀이")
    @PostMapping("/submit")
    public ResponseEntity<Submit.Response> submit(@RequestBody Submit.Request request) {
        return ResponseEntity.ok(answerService.submit(request));
    }

    @Operation(summary = "푼 문제 조회하기", description = "풀었던 문제를 조회합니다.", tags = "풀이")
    @GetMapping("/solved/{id}")
    public ResponseEntity<AnswerDto> getSolved(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.getSolved(id));
    }

}
