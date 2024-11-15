package pull_up.api.deprecated;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.member.dto.MemberAnswerResponseDto;
import pull_up.api.member.dto.MemberAnswerResultDto;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.domain.deprecated.DeprecatedService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DeprecatedController {

    private final DeprecatedService deprecatedService;

    /**
     * @deprecated not use
     */
    @Operation(summary = "사용자 답변 저장(사용X)", description = "사용자의 문제에 대한 답변을 저장하고 정답 여부를 확인합니다.", tags = "Deprecated")
    @PostMapping("/memberAnswers")
    public ResponseEntity<MemberAnswerResultDto> saveAnswer(@RequestBody MemberDto memberDTO,
                                                            @RequestBody ProblemDto problemDTO,
                                                            @RequestBody ExamInformationDto examInformationDTO,
                                                            @RequestParam String chosenAnswer) {
        MemberAnswerResultDto memberAnswer = deprecatedService.saveMemberAnswer(memberDTO, problemDTO, examInformationDTO, chosenAnswer);
        return ResponseEntity.ok(memberAnswer);
    }

    /**
     * @deprecated not use
     */
    @Operation(summary = "틀린 문제 조회(사용x)", description = "사용자가 푼 문제 중 틀린 문제를 조회합니다.", tags = "Deprecated")
    @GetMapping("/memberAnswers/incorrect")
    public ResponseEntity<List<MemberAnswerResultDto>> getIncorrectAnswers(@RequestBody MemberDto memberDTO,
                                                                           @RequestParam(required = false) String category,
                                                                           @RequestParam(required = false) String entry,
                                                                           @RequestParam(required = false) String type) {
        List<MemberAnswerResultDto> incorrectAnswers = deprecatedService.getIncorrectAnswers(memberDTO, category, entry, type);
        return ResponseEntity.ok(incorrectAnswers);
    }


    /**
     * @deprecated see AnswerController.submit()
     */
    @Operation(summary = "문제 답안 저장하기", description = "회원의 문제 답안을 저장합니다.", tags = "Deprecated")
    @PostMapping("/members/answer")
    public ResponseEntity<MemberAnswerResultDto> saveAnswer(@RequestBody MemberAnswerResponseDto memberAnswerResponseDto) {
        MemberAnswerResultDto savedAnswer = deprecatedService.saveAnswer(memberAnswerResponseDto);
        return ResponseEntity.ok(savedAnswer);
    }

    /**
     * @deprecated see ProblemController.getList
     */
    @Operation(summary = "문제 리스트 조회(골고루 및 유형별)", description = "회원이 저장한 답안에 대한 문제 리스트를 조회합니다.", tags = "Deprecated")
    @GetMapping("/problems/v0")
    public ResponseEntity<List<MemberAnswerResultDto>> getProblemList(
            @RequestParam Long memberId,
            @RequestParam(required = false) String entry,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type) {
        List<MemberAnswerResultDto> problems = deprecatedService.getProblemList(memberId, entry, category, type);
        return ResponseEntity.ok(problems);
    }

}
