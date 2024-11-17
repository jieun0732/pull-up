package pull_up.api.member;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.member.dto.MemberScoreDto;
import pull_up.domain.member.MemberService;

@Slf4j
@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "멤버 조회", description = "멤버에 대해 조회합니다.", tags = "Deprecated")
    @GetMapping("/{id}")
    public ResponseEntity<MemberScoreDto> getMemberById(@PathVariable Long id) {
        return new ResponseEntity<>(memberService.getMemberById(id), HttpStatus.OK);
    }

    @Operation(summary = "튜토리얼 확인", description = "튜토리얼 확인 여부를 확인합니다.", tags = "Deprecated")
    @PutMapping("/{id}/access-check")
    public ResponseEntity<MemberDto> updateAccessCheck(@PathVariable Long id) {
        return new ResponseEntity<>(memberService.updateAccessCheck(id), HttpStatus.OK);
    }

    @Operation(summary = "멤버 탈퇴(soft)", description = "멤버를 탈퇴시킵니다.", tags = "Deprecated")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "멤버 탈퇴(hard)", description = "멤버를 탈퇴시킵니다. DB에서 관련된 데이터를 모두 삭제합니다.", tags = "Deprecated")
    @DeleteMapping("/{id}/delete/hard")
    public ResponseEntity<String> deleteMemberHard(@PathVariable Long id) {
        memberService.deleteMemberHard(id);
        return new ResponseEntity<>("delete member " + id + " successfully.", HttpStatus.OK);
    }
}
