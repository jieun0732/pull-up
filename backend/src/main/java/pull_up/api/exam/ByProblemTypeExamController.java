package pull_up.api.exam;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.dto.Start;

@RestController
@RequestMapping("/api/exams/by-problem-type")
@RequiredArgsConstructor
public class ByProblemTypeExamController {

    private final ExamService examService;

    @Operation(summary = "시험 시작", description = "시험을 시작합니다.", tags = "유형별")
    @PostMapping("/start")
    public ResponseEntity<Start.Response> start(@RequestBody Start.ByProblemTypeRequest byProblemTypeRequest) {
        return new ResponseEntity<>(examService.start(byProblemTypeRequest), HttpStatus.OK);
    }
}
