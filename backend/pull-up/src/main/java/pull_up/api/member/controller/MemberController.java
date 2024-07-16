package pull_up.api.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.member.service.MemberService;
import pull_up.global.common.response.BaseResponse;

/**
 * 멤버 controller.
 */
@Slf4j
@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 특정 ID를 가진 Member를 조회하는 엔드포인트.
     */
    @Operation(summary = "멤버 조회", description = "멤버에 대해 조회합니다.")
    @GetMapping("/{id}")
    public BaseResponse<MemberDto> getMemberById(@PathVariable Long id) {
        return BaseResponse.success(HttpStatus.OK.value(), "멤버 조회에 성공하였습니다.", memberService.getMemberById(id));
    }

    /**
     * 특정 ID를 가진 Member의 accessCheck을 true로 변경하는 엔드포인트.
     */
    @Operation(summary = "튜토리얼 확인", description = "튜토리얼 확인 여부를 확인합니다.")
    @PutMapping("/{id}/access-check")
    public BaseResponse<MemberDto> updateAccessCheck(@PathVariable Long id) {
        return BaseResponse.success(HttpStatus.OK.value(), "튜토리얼을 확인하였습니다.",memberService.updateAccessCheck(id));
    }
}
