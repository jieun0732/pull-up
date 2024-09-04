package pull_up.api.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.member.dto.MemberAnswerDto;
import pull_up.api.member.dto.MemberAnswerResultDto;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.member.service.MemberAnswerService;
import pull_up.api.problem.dto.ProblemDto;

import java.util.List;

@RestController
@RequestMapping("/memberAnswers")
public class MemberAnswerController {

    @Autowired
    private MemberAnswerService memberAnswerService;

    @Operation(summary = "사용자 답변 저장(사용X)", description = "사용자의 문제에 대한 답변을 저장하고 정답 여부를 확인합니다.")
    @PostMapping
    public ResponseEntity<MemberAnswerResultDto> saveAnswer(@RequestBody MemberDto memberDTO,
                                                      @RequestBody ProblemDto problemDTO,
                                                      @RequestBody ExamInformationDto examInformationDTO,
                                                      @RequestParam String chosenAnswer) {
        MemberAnswerResultDto memberAnswer = memberAnswerService.saveMemberAnswer(memberDTO, problemDTO, examInformationDTO, chosenAnswer);
        return ResponseEntity.ok(memberAnswer);
    }

    @Operation(summary = "틀린 문제 조회(사용x)", description = "사용자가 푼 문제 중 틀린 문제를 조회합니다.")
    @GetMapping("/incorrect")
    public ResponseEntity<List<MemberAnswerResultDto>> getIncorrectAnswers(@RequestBody MemberDto memberDTO,
                                                                     @RequestParam(required = false) String category,
                                                                     @RequestParam(required = false) String entry,
                                                                     @RequestParam(required = false) String type) {
        List<MemberAnswerResultDto> incorrectAnswers = memberAnswerService.getIncorrectAnswers(memberDTO, category, entry, type);
        return ResponseEntity.ok(incorrectAnswers);
    }


    @Operation(summary = "모의고사 외 문제에 대한 사용자 답안 생성(회원가입 시 1회 필요)", description = "회원에 대해 category가 '모의고사'가 아닌 문제들에 대해 빈 답안을 생성합니다.")
    @PostMapping("/problems/problem-answers")
    public ResponseEntity<Void> createMemberAnswersForNonMockExamProblems(@RequestParam Long memberId) {
        memberAnswerService.createMemberAnswersForNonMockExamProblems(memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
