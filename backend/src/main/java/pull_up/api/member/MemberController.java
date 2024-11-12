package pull_up.api.member;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.member.dto.MemberScoreDto;
import pull_up.domain.member.MemberService;
import pull_up.global.response.BaseResponse;

/**
 * 멤버 controller.
 */
@Slf4j
@RequestMapping("/api/pull-up/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 특정 ID를 가진 Member를 조회하는 엔드포인트.
     */
    @Operation(summary = "멤버 조회", description = "멤버에 대해 조회합니다.", tags = "멤버")
    @GetMapping("/{id}")
    public BaseResponse<MemberScoreDto> getMemberById(@PathVariable Long id) {
        return BaseResponse.success(HttpStatus.OK.value(), "멤버 조회에 성공하였습니다.", memberService.getMemberById(id));
    }

    /**
     * 특정 ID를 가진 Member의 accessCheck을 true로 변경하는 엔드포인트.
     */
    @Operation(summary = "튜토리얼 확인", description = "튜토리얼 확인 여부를 확인합니다.", tags = "멤버")
    @PutMapping("/{id}/access-check")
    public BaseResponse<MemberDto> updateAccessCheck(@PathVariable Long id) {
        return BaseResponse.success(HttpStatus.OK.value(), "튜토리얼을 확인하였습니다.",memberService.updateAccessCheck(id));
    }


    /**
     * 멤버 탈퇴.
     */
    @Operation(summary = "멤버 탈퇴(soft)", description = "멤버를 탈퇴시킵니다.", tags = "멤버")
    @DeleteMapping("/{id}/delete")
    public BaseResponse<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return BaseResponse.success(HttpStatus.OK.value(), "멤버 탈퇴를 완료하였습니다.", null);
    }

    @Operation(summary = "멤버 탈퇴(hard)", description = "멤버를 탈퇴시킵니다. DB에서 관련된 데이터를 모두 삭제합니다.", tags = "멤버")
    @DeleteMapping("/{id}/delete/hard")
    public ResponseEntity<String> deleteMemberHard(@PathVariable Long id) {
        memberService.deleteMemberHard(id);
        return new ResponseEntity<>("delete member " + id + " successfully.", HttpStatus.OK);
    }
}
