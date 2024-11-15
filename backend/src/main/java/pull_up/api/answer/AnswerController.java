package pull_up.api.answer;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pull_up.api.answer.dto.CreateAnswer;
import pull_up.domain.answer.AnswerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerService answerService;

    @Operation(summary = "문제 답안 제출하기", description = "해당 문제의 답안을 제출합니다.", tags = "유형별/골고루")
    @PostMapping("/submit")
    public ResponseEntity<CreateAnswer.Response> saveAnswer(@RequestBody CreateAnswer.Request request) {
        return ResponseEntity.ok(answerService.submit(request));
    }


}
