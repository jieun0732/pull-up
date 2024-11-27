package pull_up.api;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pull_up.domain.member.MemberService;
import pull_up.domain.member.dto.MemberInfo;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "멤버정보 조회", description = "사용자 정보를 조회합니다.", tags = "멤버")
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfo.Response> getMemberInfo(@PathVariable Long memberId) {
        return new ResponseEntity<>(memberService.getMemberInfo(memberId), HttpStatus.OK);
    }
}
