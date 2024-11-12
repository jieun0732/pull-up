package pull_up.api.problem;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pull_up.domain.problem.ProblemService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pull-up/problems")
public class ProblemController {

    private final ProblemService problemService;

    @Operation(summary = "문제 삭제(hard)", description = "DB 상의 모든 문제를 삭제합니다.", tags = "문제")
    @DeleteMapping("/hard")
    public ResponseEntity<String> deleteAllProblem() {
        problemService.deleteAllProblem();

        return new ResponseEntity<>("all problem deleted successfully.", HttpStatus.OK);
    }
}
