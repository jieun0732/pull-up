package pull_up.api.exam;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.dto.Start;

@RestController
@RequestMapping("/api/exams/evenly")
@RequiredArgsConstructor
public class EvenlyExamController {

    private final ExamService examService;

    @Operation(summary = "시험 시작", description = "시험을 시작합니다.", tags = "골고루")
    @PostMapping("/start")
    public ResponseEntity<Start.Response> start(@RequestBody Start.EvenlyRequest evenlyRequest) {
        return new ResponseEntity<>(examService.start(evenlyRequest), HttpStatus.OK);
    }
}
