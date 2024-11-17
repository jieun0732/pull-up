package pull_up.api.exam.evenly;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.domain.exam.EvenlyExamService;
import pull_up.domain.exam.dto.Next;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.exam.dto.Submit;

@RestController
@RequestMapping("/api/exams/evenly")
@RequiredArgsConstructor
public class EvenlyExamController {

    private final EvenlyExamService evenlyExamService;

    @Operation(summary = "시험 시작", description = "골고루 시험을 시작합니다.", tags = "골고루")
    @PostMapping("/start")
    public ResponseEntity<Start.Response> start(@RequestBody Start.Request request) {
        return new ResponseEntity<>(evenlyExamService.start(request), HttpStatus.OK);
    }

    @Operation(summary = "답안 제출", description = "골고루 답안을 제출합니다.", tags = "골고루")
    @PostMapping("/submit")
    public ResponseEntity<Submit.Response> submit(@RequestBody Submit.Request request) {
        return new ResponseEntity<>(evenlyExamService.submit(request), HttpStatus.OK);
    }

    @Operation(summary = "다음문제 확인", description = "다음 문제를 확인합니다.", tags = "골고루")
    @GetMapping("/next/{examId}")
    public ResponseEntity<Next.Response> next(@PathVariable Long examId, @RequestParam Integer problemNumber) {
        return new ResponseEntity<>(evenlyExamService.next(examId, problemNumber), HttpStatus.OK);
    }
}
