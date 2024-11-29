package pull_up.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.api.dto.ListDto;
import pull_up.api.dto.MessageDto;
import pull_up.domain.member.dto.SolvedInfo;
import pull_up.domain.member.MemberService;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.dto.IncorrectQueryDto;
import pull_up.domain.member.dto.MemberInfo;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "[골고루, 유형별] 푼 시험 확인", description = "골고루 / 유형별 시험 현황을 확인합니다.", tags = "멤버")
    @GetMapping("/entry-exam/{entry}/solved")
    public ResponseEntity<SolvedInfo.ByEntryResponse> getSolvedInfo(@RequestParam Long memberId, @PathVariable Entry entry) {
        return new ResponseEntity<>(memberService.getSolvedInfo(memberId, entry), HttpStatus.OK);
    }

    @Operation(summary = "[모의고사] 푼 시험 확인", description = "모의고사 시험 현황을 확인합니다.", tags = "멤버")
    @GetMapping("/mock-exam/solved")
    public ResponseEntity<SolvedInfo.MockExamResponse> getMockExamSolvedInfo(@RequestParam Long memberId) {
        return new ResponseEntity<>(memberService.getSolvedInfo(memberId), HttpStatus.OK);
    }

    @Operation(summary = "멤버정보 조회", description = "사용자 정보를 조회합니다.", tags = "멤버")
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfo.Response> getMemberInfo(@PathVariable Long memberId) {
        return new ResponseEntity<>(memberService.getMemberInfo(memberId), HttpStatus.OK);
    }

    @Operation(summary = "[골고루, 유형별] 멤버 틀린문제 조회", description = "사용자의 틀린문제를 조회합니다.", tags = "멤버")
    @GetMapping("/incorrect/{memberId}")
    public ResponseEntity<ListDto<IncorrectQueryDto>> getMemberIncorrectAnswer(@PathVariable Long memberId) {
        return new ResponseEntity<>(memberService.getIncorrect(memberId), HttpStatus.OK);
    }

    @Operation(summary = "멤버 튜토리얼 체크", description = "사용자의 튜토리얼 현황을 완료로 변경합니다.", tags = "멤버")
    @PatchMapping("/tutorial/{memberId}")
    public ResponseEntity<MessageDto> patchTutorial(@PathVariable Long memberId) {
        return new ResponseEntity<>(memberService.tutorialCheck(memberId), HttpStatus.OK);
    }
}
