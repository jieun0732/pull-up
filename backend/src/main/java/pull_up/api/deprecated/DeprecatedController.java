package pull_up.api.deprecated;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.member.dto.MemberAnswerResultDto;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.domain.member.MemberAnswerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memberAnswers")
public class DeprecatedController {

    private final MemberAnswerService memberAnswerService;

    @Operation(summary = "사용자 답변 저장(사용X)", description = "사용자의 문제에 대한 답변을 저장하고 정답 여부를 확인합니다.", tags = "Deprecated")
    @PostMapping
    public ResponseEntity<MemberAnswerResultDto> saveAnswer(@RequestBody MemberDto memberDTO,
                                                            @RequestBody ProblemDto problemDTO,
                                                            @RequestBody ExamInformationDto examInformationDTO,
                                                            @RequestParam String chosenAnswer) {
        MemberAnswerResultDto memberAnswer = memberAnswerService.saveMemberAnswer(memberDTO, problemDTO, examInformationDTO, chosenAnswer);
        return ResponseEntity.ok(memberAnswer);
    }

    @Operation(summary = "틀린 문제 조회(사용x)", description = "사용자가 푼 문제 중 틀린 문제를 조회합니다.", tags = "Deprecated")
    @GetMapping("/incorrect")
    public ResponseEntity<List<MemberAnswerResultDto>> getIncorrectAnswers(@RequestBody MemberDto memberDTO,
                                                                           @RequestParam(required = false) String category,
                                                                           @RequestParam(required = false) String entry,
                                                                           @RequestParam(required = false) String type) {
        List<MemberAnswerResultDto> incorrectAnswers = memberAnswerService.getIncorrectAnswers(memberDTO, category, entry, type);
        return ResponseEntity.ok(incorrectAnswers);
    }

}
