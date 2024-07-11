package pull_up.api.exam.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.exam.dto.ExamProblemDto;
import pull_up.api.exam.service.ExamService;
import pull_up.api.member.dto.MemberDto;

import java.util.List;

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

    @Operation(summary = "시험 정보 생성", description = "회원 정보를 기반으로 새로운 시험 정보를 생성하고 관련 문제를 할당합니다.")
    @PostMapping
    public ResponseEntity<ExamInformationDto> createExam(@RequestBody MemberDto memberDto,
                                                         @RequestParam String entry,
                                                         @RequestParam String category,
                                                         @RequestParam String type) {
        ExamInformationDto examInformation = examService.createExamInformation(memberDto, entry, category, type);
        return ResponseEntity.ok(examInformation);
    }

    @Operation(summary = "시험 문제 조회", description = "시험 ID를 기반으로 시험에 할당된 모든 문제를 조회합니다.")
    @GetMapping("/{examId}/problems")
    public ResponseEntity<List<ExamProblemDto>> getExamProblems(@PathVariable Long examId) {
        List<ExamProblemDto> examProblems = examService.getExamProblems(examId);
        return ResponseEntity.ok(examProblems);
    }

    @Operation(summary = "모든 시험 정보 조회", description = "모든 시험 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ExamInformationDto>> getAllExams() {
        List<ExamInformationDto> exams = examService.getAllExamInformation();
        return ResponseEntity.ok(exams);
    }

    @Operation(summary = "특정 시험 정보 조회", description = "특정 시험 ID를 기반으로 시험 정보를 조회합니다.")
    @GetMapping("/{examId}")
    public ResponseEntity<ExamInformationDto> getExam(@PathVariable Long examId) {
        ExamInformationDto exam = examService.getExamInformation(examId);
        return ResponseEntity.ok(exam);
    }
}