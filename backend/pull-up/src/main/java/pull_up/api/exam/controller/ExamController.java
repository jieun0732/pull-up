package pull_up.api.exam.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pull_up.api.exam.dto.CreatedExamInformationResponseDto;
import pull_up.api.exam.dto.CreatedExamInformationResultDto;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.exam.dto.ExamProblemDto;
import pull_up.api.exam.service.ExamService;
import pull_up.api.member.dto.IncorrectAnswerDto;
import pull_up.api.member.dto.MemberAnswerDto;
import pull_up.api.problem.dto.ProblemDto;

/**
 * 시험 관련 요청을 처리하는 컨트롤러.
 */
@Slf4j
@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {

    @Autowired
    private ExamService examService;

    @Operation(summary = "문제 리스트 조회", description = "회원이 저장한 답안에 대한 문제 리스트를 조회합니다.")
    @GetMapping("/problems")
    public ResponseEntity<List<MemberAnswerDto>> getProblemList(
        @RequestParam Long memberId,
        @RequestParam(required = false) String entry,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String type) {
        List<MemberAnswerDto> problems = examService.getProblemList(memberId, entry, category, type);
        return ResponseEntity.ok(problems);
    }

    @Operation(summary = "문제 답안 저장하기", description = "회원의 문제 답안을 저장합니다.")
    @PostMapping("/answer")
    public ResponseEntity<MemberAnswerDto> saveAnswer(@RequestBody MemberAnswerDto memberAnswerDto) {
        MemberAnswerDto savedAnswer = examService.saveAnswer(memberAnswerDto);
        return ResponseEntity.ok(savedAnswer);
    }

    @Operation(summary = "다시 풀기", description = "회원의 문제 답안을 초기화합니다.")
    @PostMapping("/reset")
    public ResponseEntity<Void> resetAnswers(
        @RequestParam Long memberId,
        @RequestParam(required = false) String entry,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String type) {
        examService.resetAnswers(memberId, entry, category, type);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이어 풀기", description = "회원이 아직 풀지 않은 문제를 조회합니다.")
    @GetMapping("/next")
    public ResponseEntity<MemberAnswerDto> getNextUnanswered(
        @RequestParam Long memberId,
        @RequestParam(required = false) String entry,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String type) {
        MemberAnswerDto nextUnanswered = examService.getNextUnanswered(memberId, entry, category, type);
        return ResponseEntity.ok(nextUnanswered);
    }

    @Operation(summary = "모의고사 문제 리스트 조회", description = "모의고사 문제 리스트를 조회합니다.")
    @GetMapping("/mock-exam/problems")
    public ResponseEntity<List<ProblemDto>> getMockExamProblems() {
        List<ProblemDto> problems = examService.getMockExamProblems();
        return ResponseEntity.ok(problems);
    }

    @Operation(summary = "모의고사 시작하기", description = "모의고사를 시작합니다.")
    @PostMapping("/mock-exam/start")
    public ResponseEntity<CreatedExamInformationResultDto> startMockExam(@RequestBody CreatedExamInformationResponseDto createdExamInformationDto) {
        CreatedExamInformationResultDto startedExam = examService.startMockExam(createdExamInformationDto);
        return ResponseEntity.ok(startedExam);
    }

    /**
     * ExamProblem ID를 통해 문제를 조회합니다.
     *
     * @param examProblemId ExamProblem ID
     * @return 문제 정보
     */
    @GetMapping("/problem/{examProblemId}")
    public ResponseEntity<ProblemDto> getProblemByExamProblemId(@PathVariable Long examProblemId) {
        ProblemDto problemDto = examService.getProblemByExamProblemId(examProblemId);
        return ResponseEntity.ok(problemDto);
    }

    @Operation(summary = "모의고사 답안 저장하기", description = "모의고사 문제의 답안을 저장합니다.")
    @PostMapping("/mock-exam/answer")
    public ResponseEntity<ExamProblemDto> saveMockExamAnswer(@RequestBody ExamProblemDto examProblemDto) {
        ExamProblemDto savedAnswer = examService.saveMockExamAnswer(examProblemDto);
        return ResponseEntity.ok(savedAnswer);
    }

    @Operation(summary = "모의고사 완료 및 점수 저장하기", description = "모의고사를 완료하고 점수를 저장합니다.")
    @PostMapping("/mock-exam/complete")
    public ResponseEntity<ExamInformationDto> completeMockExam(@RequestBody ExamInformationDto examInformationDto) {
        ExamInformationDto completedExam = examService.completeMockExam(examInformationDto);
        return ResponseEntity.ok(completedExam);
    }

    @Operation(summary = "틀린 문제 리스트 조회하기", description = "회원이 틀린 문제 리스트를 조회합니다.")
    @GetMapping("/incorrect-answers")
    public ResponseEntity<List<IncorrectAnswerDto>> getIncorrectAnswers(@RequestParam Long memberId) {
        List<IncorrectAnswerDto> incorrectAnswers = examService.getIncorrectAnswers(memberId);
        return ResponseEntity.ok(incorrectAnswers);
    }

    @Operation(summary = "틀린 문제 상세 조회하기", description = "회원이 틀린 문제의 상세 정보를 조회합니다.")
    @GetMapping("/incorrect-answers/{id}")
    public ResponseEntity<IncorrectAnswerDto> getIncorrectAnswer(@PathVariable Long id) {
        IncorrectAnswerDto incorrectAnswer = examService.getIncorrectAnswer(id);
        return ResponseEntity.ok(incorrectAnswer);
    }
}