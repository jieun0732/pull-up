package pull_up.api.deprecated;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.domain.deprecated.MemberAnswerService;

@RestController
@RequestMapping("/api/pull-up/memberAnswers")
public class MemberAnswerController {

    @Autowired
    private MemberAnswerService memberAnswerService;

    @Operation(summary = "모의고사 외 문제에 대한 사용자 답안 생성(회원가입 시 1회 필요)", description = "회원에 대해 category가 '모의고사'가 아닌 문제들에 대해 빈 답안을 생성합니다.", tags = "Deprecated")
    @PostMapping("/problems/problem-answers")
    public ResponseEntity<Void> createMemberAnswersForNonMockExamProblems(@RequestParam Long memberId) {
        memberAnswerService.createMemberAnswersForNonMockExamProblems(memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
