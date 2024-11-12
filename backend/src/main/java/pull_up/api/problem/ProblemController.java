package pull_up.api.problem;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pull_up.domain.problem.ProblemService;
import pull_up.global.dto.MessageDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pull-up/problems")
public class ProblemController {

    private final ProblemService problemService;

    @Operation(summary = "문제 추가", description = "문제를 추가합니다.", tags = "문제")
    @PostMapping
    public ResponseEntity<?> createProblem() {
        problemService.deleteAllProblem();

        return new ResponseEntity<>("All problem deleted successfully.", HttpStatus.OK);
    }

    @Operation(summary = "문제 삭제(hard)", description = "DB 상의 모든 문제를 삭제합니다.", tags = "문제")
    @DeleteMapping("/hard")
    public ResponseEntity<MessageDto> deleteAllProblem() {
        problemService.deleteAllProblem();

        return new ResponseEntity<>(new MessageDto("All problem deleted successfully."), HttpStatus.OK);
    }
}
