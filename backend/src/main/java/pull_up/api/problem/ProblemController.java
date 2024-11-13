package pull_up.api.problem;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.api.problem.dto.CreateProblem;
import pull_up.domain.problem.ProblemService;
import pull_up.global.dto.MessageDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pull-up/problems")
public class ProblemController {

    private final ProblemService problemService;

    @Operation(summary = "문제 추가", description = "문제를 추가합니다.", tags = "문제")
    @PostMapping
    public ResponseEntity<MessageDto> createProblem(@RequestBody CreateProblem.Request createProblemReq) {

        return new ResponseEntity<>(problemService.createProblem(createProblemReq), HttpStatus.OK);
    }

    @Operation(summary = "문제 추가(format)", description = "스프레드 시트의 포맷에 맞게 문제를 추가합니다.", tags = "문제")
    @PostMapping("/format")
    public ResponseEntity<MessageDto> createProblemWithFormat(@RequestBody String formatString) {

        return new ResponseEntity<>(problemService.createProblem(formatString), HttpStatus.OK);
    }

    @Operation(summary = "문제 삭제(hard)", description = "DB 상의 모든 문제를 삭제합니다.", tags = "문제")
    @DeleteMapping("/hard")
    public ResponseEntity<MessageDto> deleteAllProblem() {
        problemService.deleteAllProblem();

        return new ResponseEntity<>(new MessageDto("All problem deleted successfully."), HttpStatus.OK);
    }
}
