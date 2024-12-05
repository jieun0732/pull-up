package pull_up.api.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.api.dto.MessageDto;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.dto.*;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @Operation(summary = "시험 시작", description = "유형별 문제의 첫번째 문제를 가지고 시험을 시작합니다.", tags = "골고루")
    @PostMapping("/evenly/start")
    public ResponseEntity<Start.Response> start(@RequestBody Start.EvenlyRequest evenlyRequest) {
        return new ResponseEntity<>(examService.start(evenlyRequest), HttpStatus.OK);
    }

    @Operation(summary = "시험 시작", description = "시험을 시작합니다.", tags = "유형별")
    @PostMapping("/by-problem-type/start")
    public ResponseEntity<Start.Response> start(@RequestBody Start.ByProblemTypeRequest byProblemTypeRequest) {
        return new ResponseEntity<>(examService.start(byProblemTypeRequest), HttpStatus.OK);
    }

    @Operation(summary = "시험 시작", description = "시험을 시작합니다.", tags = "모의고사")
    @PostMapping("/mock-exam/start")
    public ResponseEntity<Start.Response> start(@RequestBody Start.MockExamRequest mockExamRequest) {
        return new ResponseEntity<>(examService.start(mockExamRequest), HttpStatus.OK);
    }

    @Operation(summary = "다음문제 확인", description = "다음 문제를 확인합니다.", tags = {"골고루", "유형별", "모의고사"})
    @GetMapping("/next/{examId}")
    public ResponseEntity<Next.Response> next(@PathVariable Long examId, @RequestParam Integer problemNumber) {
        return new ResponseEntity<>(examService.next(examId, problemNumber), HttpStatus.OK);
    }

    @Operation(summary = "답안 제출", description = "답안을 제출합니다.", tags = {"골고루", "유형별"})
    @PostMapping("/submit")
    public ResponseEntity<Explanation> submit(@RequestBody Submit.Request request) {
        return new ResponseEntity<>(examService.submit(request), HttpStatus.OK);
    }

    @Operation(summary = "이어하기", description = "시험을 이어서 진행합니다.", tags = {"골고루", "유형별"})
    @GetMapping("/continue/{examId}")
    public ResponseEntity<Next.Response> continueExam(@PathVariable Long examId) {
        return new ResponseEntity<>(examService.continueExam(examId), HttpStatus.OK);
    }

    @Operation(summary = "리셋하기", description = "시험 정보를 삭제합니다.", tags = {"골고루", "유형별", "모의고사"})
    @DeleteMapping("/reset/{examId}")
    public ResponseEntity<MessageDto> reset(@PathVariable Long examId) {
        return new ResponseEntity<>(examService.reset(examId), HttpStatus.OK);
    }

    @Operation(summary = "채점 및 결과 레포트 조회", description = "답안을 채점 후 결과 레포트를 확인합니다.", tags = "모의고사")
    @PostMapping("/mock-exam/grade")
    public ResponseEntity<Report.mockExam> grade(@RequestBody Grade.Request request) {
        return new ResponseEntity<>(examService.grade(request), HttpStatus.OK);
    }

    @Operation(summary = "결과 레포트 조회", description = "결과 레포트를 확인합니다.", tags = "모의고사")
    @GetMapping("/mock-exam/report/{examId}")
    public ResponseEntity<Report.mockExam> getMockExamReport(@PathVariable Long examId) {
        return new ResponseEntity<>(examService.getMockExamReport(examId), HttpStatus.OK);
    }

    @Operation(summary = "시험 결과 확인", description = "시험 결과를 확인합니다.", tags = "모의고사")
    @GetMapping("/mock-exam/result/{examId}")
    public ResponseEntity<Result.MockExamResponse> getMockExamResult(@PathVariable Long examId) {
        return new ResponseEntity<>(examService.getMockExamResult(examId), HttpStatus.OK);
    }

    @Operation(summary = "[Deprecated]시험 결과 확인", description = "시험 결과를 확인합니다.", tags = {"골고루", "유형별"})
    @PatchMapping("/end/{examId}")
    public ResponseEntity<Result.ByEntryResponse> end(@PathVariable Long examId) {
        return new ResponseEntity<>(examService.getEntryExamResult(examId), HttpStatus.OK);
    }

    @Operation(summary = "시험 결과 확인", description = "시험 결과를 확인합니다.", tags = {"골고루", "유형별"})
    @GetMapping("/result/{examId}")
    public ResponseEntity<Result.ByEntryResponse> getEntryExamResult(@PathVariable Long examId) {
        return new ResponseEntity<>(examService.getEntryExamResult(examId), HttpStatus.OK);
    }
}
