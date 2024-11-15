package pull_up.api.exam;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pull_up.api.exam.dto.*;
import pull_up.domain.exam.ExamService;
import pull_up.api.member.dto.MemberAnswerIndexDto;
import pull_up.api.member.dto.MemberAnswerResponseDto;
import pull_up.api.member.dto.MemberAnswerResultDto;
import pull_up.api.member.dto.MemberAnswerSolvedDto;
import pull_up.api.problem.dto.ProblemResultDto;
import pull_up.api.problem.dto.ProblemSolvedDto;
import pull_up.api.problem.dto.ProblemTimeResultDto;
import pull_up.api.problem.dto.ProblemTypeResultDto;
import pull_up.api.problem.dto.ProblemTypeSummaryDto;
import pull_up.global.dto.ListDto;

/**
 * 시험 관련 요청을 처리하는 컨트롤러.
 */
@Slf4j
@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @Operation(summary = "문제 리스트 id값 조회(골고루 및 유형별)", description = "회원이 저장한 답안에 대한 문제 id 리스트를 조회합니다.", tags = "유형별/골고루")
    @GetMapping("/problemsIndex")
    public ResponseEntity<List<MemberAnswerIndexDto>> getProblemIndexList(
        @RequestParam Long memberId,
        @RequestParam(required = false) String entry,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String type) {
        List<MemberAnswerIndexDto> problems = examService.getProblemIndexList(memberId, entry, category, type);
        return ResponseEntity.ok(problems);
    }

    @Operation(summary = "문제 조회(골고루 및 유형별)", description = "MemberAnswer의 id를 이용하여 문제를 조회합니다.", tags = "유형별/골고루")
    @GetMapping("/problem")
    public ResponseEntity<MemberAnswerResultDto> getProblemByMemberAnswerId(
        @RequestParam Long memberAnswerId) {
        MemberAnswerResultDto memberAnswerDto = examService.getProblemByMemberAnswerId(memberAnswerId);
        return ResponseEntity.ok(memberAnswerDto);
    }

    @Operation(summary = "수리 문제 요약 조회", description = "회원이 저장한 답안에 대한 수리 문제 요약을 조회합니다.", tags = "유형별/골고루")
    @GetMapping("/problems/math")
    public ResponseEntity<List<ProblemTypeSummaryDto>> getMathProblemsSummary(
        @RequestParam Long memberId) {
        List<ProblemTypeSummaryDto> summaries = examService.getMathProblemsSummary(memberId);
        return ResponseEntity.ok(summaries);
    }

    @Operation(summary = "언어 문제 요약 조회", description = "회원이 저장한 답안에 대한 언어 문제 요약을 조회합니다.", tags = "유형별/골고루")
    @GetMapping("/problems/language")
    public ResponseEntity<List<ProblemTypeSummaryDto>> getLanguageProblemsSummary(
        @RequestParam Long memberId) {
        List<ProblemTypeSummaryDto> summaries = examService.getLanguageProblemsSummary(memberId);
        return ResponseEntity.ok(summaries);
    }

    @Operation(summary = "추리 문제 요약 조회", description = "회원이 저장한 답안에 대한 추리 문제 요약을 조회합니다.", tags = "유형별/골고루")
    @GetMapping("/problems/reasoning")
    public ResponseEntity<List<ProblemTypeSummaryDto>> getReasoningProblemsSummary(
        @RequestParam Long memberId) {
        List<ProblemTypeSummaryDto> summaries = examService.getReasoningProblemsSummary(memberId);
        return ResponseEntity.ok(summaries);
    }

    @Operation(summary = "문제 정답 여부 및 선택된 답안 조회", description = "회원이 저장한 답안에 대한 문제의 정답 여부와 선택된 답안을 조회합니다.", tags = "유형별/골고루")
    @GetMapping("/problems/correctness")
    public ResponseEntity<List<MemberAnswerSolvedDto>> getProblemSolvedList(
        @RequestParam Long memberId,
        @RequestParam(required = false) String entry,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String type) {
        List<MemberAnswerSolvedDto> correctnessList = examService.getProblemSolvedList(memberId, entry, category, type);
        return ResponseEntity.ok(correctnessList);
    }

    @Operation(summary = "다시 풀기", description = "회원의 문제 답안을 초기화합니다.", tags = "유형별/골고루")
    @PutMapping("/reset")
    public ResponseEntity<Void> resetAnswers(
        @RequestParam Long memberId,
        @RequestParam(required = false) String entry,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String type) {
        examService.resetAnswers(memberId, entry, category, type);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이어 풀기", description = "회원이 아직 풀지 않은 문제를 조회합니다.", tags = "유형별/골고루")
    @GetMapping("/next")
    public ResponseEntity<MemberAnswerResultDto> getNextUnanswered(
        @RequestParam Long memberId,
        @RequestParam(required = false) String entry,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String type) {
        MemberAnswerResultDto nextUnanswered = examService.getNextUnanswered(memberId, entry,
            category, type);
        return ResponseEntity.ok(nextUnanswered);
    }

    @Operation(summary = "보유 모의고사 문제 전체 조회", description = "보유하고 있는 모의고사 문제를 모두 조회합니다.", tags = "모의고사")
    @GetMapping("/mock-exam/problemsList")
    public ResponseEntity<List<ProblemResultDto>> getMockExamProblems() {
        List<ProblemResultDto> problems = examService.getMockExamProblems();
        return ResponseEntity.ok(problems);
    }

    @Operation(summary = "모의고사 시작하기", description = "모의고사를 시작합니다.", tags = "모의고사")
    @PostMapping("/mock-exam/start")
    public ResponseEntity<CreatedExamInformationResultDto> startMockExam(@RequestParam Long memberId) {
        CreatedExamInformationResultDto startedExam = examService.startMockExam(memberId);
        return ResponseEntity.ok(startedExam);
    }

    @Operation(summary = "모의고사 문제 푼 여부 조회", description = "시험 정보 ID를 기준으로 문제와 푼 여부를 조회합니다.", tags = "모의고사")
    @GetMapping("/mock-exam/solved")
    public ResponseEntity<List<ProblemSolvedDto>> getProblemsSolvedByExamInformation(
        @RequestParam Long examInformationId) {
        List<ProblemSolvedDto> problemsSolved = examService.getProblemsSolvedByExamInformation(examInformationId);
        return ResponseEntity.ok(problemsSolved);
    }

    @Operation(summary = "모의고사 id로 문제 리스트 및 정답 여부 조회 ", description = "시험 정보 ID를 기준으로 문제를 조회합니다.", tags = "모의고사")
    @GetMapping("/mock-exam/problems")
    public ResponseEntity<List<ProblemSolvedDto>> getExamProblemByExamInformation(
        @RequestParam Long examInformationId) {
        List<ProblemSolvedDto> problemsSolved = examService.getExamProblemByExamInformation(examInformationId);
        return ResponseEntity.ok(problemsSolved);
    }

    /**
     * ExamProblem ID를 통해 문제를 조회합니다.
     */
    @Operation(summary = "모의고사 문제 하나 조회(ExamProblem Id 이용)", description = "모의고사 문제 하나를 조회합니다.", tags = "모의고사")
    @GetMapping("/mock-exam/{examProblemId}")
    public ResponseEntity<ProblemTimeResultDto> getProblemByExamProblemId(@PathVariable Long examProblemId) {
        ProblemTimeResultDto problemResultDto = examService.getProblemByExamProblemId(examProblemId);
        return ResponseEntity.ok(problemResultDto);
    }

    /**
     * ExamInformation ID 및 문제 번호를 통해 문제를 조회합니다.
     */
    @Operation(summary = "모의고사 문제 하나 조회(모의고사Id + 문제 번호 이용)", description = "모의고사 문제 하나를 조회합니다.", tags = "모의고사")
    @GetMapping("/mock-exam/problem")
    public ResponseEntity<ProblemTimeResultDto> getProblemByExamInformationIdAndProblemNumber(
        @RequestParam Long examInformationId,
        @RequestParam Long problemNumber) {
        ProblemTimeResultDto problemResultDto = examService.getProblemByExamInformationIdAndProblemNumber(examInformationId, problemNumber);
        return ResponseEntity.ok(problemResultDto);
    }

    @Operation(summary = "모의고사 답안 저장하기", description = "모의고사 문제의 답안을 저장합니다.", tags = "모의고사")
    @PostMapping("/mock-exam/answer")
    public ResponseEntity<ExamProblemResultDto> saveMockExamAnswer(@RequestBody ExamProblemResponseDto examProblemResponseDto) {
        ExamProblemResultDto savedAnswer = examService.saveMockExamAnswer(examProblemResponseDto);
        return ResponseEntity.ok(savedAnswer);
    }

    @Operation(summary = "모의고사 완료 및 점수 저장하기", description = "모의고사를 완료하고 점수를 저장합니다. 수정", tags = "모의고사")
    @PostMapping("/mock-exam/{examInformationId}/complete")
    public ResponseEntity<ExamInformationDto> completeMockExam(@PathVariable Long examInformationId) {
        ExamInformationDto completedExam = examService.completeMockExam(examInformationId);
        return ResponseEntity.ok(completedExam);
    }

    @Operation(summary = "모의고사 삭제하기", description = "모의고사를 삭제합니다.", tags = "모의고사")
    @DeleteMapping("/mock-exam/{examInformationId}")
    public ResponseEntity<?> deleteMockExam(@PathVariable Long examInformationId) {
        examService.deleteMockExam(examInformationId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "틀린 문제 리스트 조회하기", description = "회원이 틀린 문제 리스트를 조회합니다.", tags = "틀린문제")
    @GetMapping("/incorrect-answers")
    public ResponseEntity<ListDto<IncorrectAnswer.Brief>> getIncorrectAnswers(@RequestParam Long memberId) {
        return ResponseEntity.ok(examService.getIncorrectAnswers(memberId));
    }

    @Operation(summary = "틀린 문제 상세 조회하기", description = "회원이 틀린 문제의 상세 정보를 조회합니다. 수정", tags = "틀린문제")
    @GetMapping("/incorrect-answers/{id}")
    public ResponseEntity<IncorrectAnswer.Detail> getIncorrectAnswer(@PathVariable Long id) {
        return ResponseEntity.ok(examService.getIncorrectAnswerDetail(id));
    }

    /**
     * 전체 모의고사의 평균 점수 구하기.
     */
    @GetMapping("/mock-exam/average-score")
    @Operation(summary = "전체 모의고사의 평균 점수 및 평균 소요시간 구하기", description = "전체 모의고사의 평균 점수 및 소요시간을 구합니다.", tags = "모의고사")
    public ResponseEntity<ExamInformationAverageScoreDto> getAverageScore() {
        ExamInformationAverageScoreDto averageScoreDto = examService.calculateAverageScore();
        return ResponseEntity.ok(averageScoreDto);
    }


    /**
     * 문제 유형별로 총 문제 수와 맞힌 문제 수를 반환합니다.
     */
    @GetMapping("/mock-exam/result-by-type")
    @Operation(summary = "문제 유형별 맞힌 문제 수", description = "문제 유형별로 총 문제 수와 맞힌 문제 수를 반환합니다. 수정", tags = "모의고사")
    public ResponseEntity<List<ProblemTypeResultDto>> getProblemTypeResults(@RequestParam Long examInformationId) {
        List<ProblemTypeResultDto> results = examService.getProblemTypeResults(examInformationId);
        return ResponseEntity.ok(results);
    }

    /**
     * 멤버의 최근 모의고사 정보 및 문제 유형별 결과를 반환합니다.
     */
    @GetMapping("/mock-exam/recent")
    @Operation(summary = "최근 모의고사 정보 및 문제 유형별 결과", description = "멤버의 가장 최근 모의고사 정보를 반환합니다. 수정", tags = "모의고사")
    public ResponseEntity<ExamInformationDetailDto> getRecentExamInformation(
        @RequestParam Long memberId) {

        return ResponseEntity.ok(examService.getRecentExamInformation(memberId));
    }

    @PostMapping("/mock-exam/grade")
    @Operation(summary = "모의고사 채점", description = "모의고사를 채점한 뒤, 채점 결과를 반환합니다.", tags = "모의고사")
    public ResponseEntity<GradeExam.Response> grade(@RequestBody GradeExam.Request request) {
        return ResponseEntity.ok(examService.grade(request));
    }
}
