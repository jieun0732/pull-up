package pull_up.api.exam;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.dto.End;
import pull_up.domain.exam.dto.Next;
import pull_up.domain.exam.dto.SolvedInfo;
import pull_up.domain.exam.dto.Submit;
import pull_up.domain.problem.Entry;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @Operation(summary = "푼 시험 확인", description = "풀었던 골고루 / 유형별 시험 현황을 확인합니다.", tags = "골고루/유형별")
    @GetMapping("/solved")
    public ResponseEntity<SolvedInfo.Response> getSolvedInfo(@RequestParam Long memberId, @RequestParam Entry entry) {
        return new ResponseEntity<>(examService.getSolvedInfo(memberId, entry), HttpStatus.OK);
    }

    @Operation(summary = "답안 제출", description = "답안을 제출합니다.", tags = "골고루/유형별")
    @PostMapping("/submit")
    public ResponseEntity<Submit.Response> submit(@RequestBody Submit.Request request) {
        return new ResponseEntity<>(examService.submit(request), HttpStatus.OK);
    }

    @Operation(summary = "다음문제 확인", description = "다음 문제를 확인합니다.", tags = "골고루/유형별")
    @GetMapping("/next/{examId}")
    public ResponseEntity<Next.Response> next(@PathVariable Long examId, @RequestParam Integer problemNumber) {
        return new ResponseEntity<>(examService.next(examId, problemNumber), HttpStatus.OK);
    }

    @Operation(summary = "시험 종료", description = "시험을 종료합니다.", tags = "골고루/유형별")
    @PatchMapping("/end/{examId}")
    public ResponseEntity<End.Response> end(@PathVariable Long examId) {
        return new ResponseEntity<>(examService.end(examId), HttpStatus.OK);
    }
}
