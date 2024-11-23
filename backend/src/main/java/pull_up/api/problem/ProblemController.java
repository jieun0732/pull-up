package pull_up.api.problem;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.domain.problem.dto.CreateProblem;
import pull_up.domain.problem.ProblemService;
import pull_up.global.dto.MessageDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemService problemService;

    @Operation(summary = "문제 추가", description = "문제를 추가합니다.", tags = "문제")
    @PostMapping
    public ResponseEntity<MessageDto> createProblem(@RequestBody CreateProblem.Request createProblemReq) {
        return new ResponseEntity<>(problemService.createProblem(createProblemReq), HttpStatus.OK);
    }

    @Operation(summary = "문제 추가(format)", description = "스프레드 시트의 포맷에 맞게 문제를 추가합니다.", tags = "문제")
    @PostMapping("/format")
    public ResponseEntity<MessageDto> createProblem(@RequestBody String formatString) {
        return new ResponseEntity<>(problemService.createProblem(formatString), HttpStatus.OK);
    }
}
