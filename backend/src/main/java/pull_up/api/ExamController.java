package pull_up.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pull_up.api.dto.MessageDto;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.dto.*;
import pull_up.domain.problem.Entry;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @Operation(summary = "푼 시험 확인", description = "골고루 / 유형별 시험 현황을 확인합니다.", tags = {"골고루", "유형별"})
    @GetMapping("/entry/{entry}/solved")
    public ResponseEntity<Solved.ByEntry.Response> getSolvedInfo(@RequestParam Long memberId, @PathVariable Entry entry) {
        return new ResponseEntity<>(examService.getSolvedInfo(memberId, entry), HttpStatus.OK);
    }

    @Operation(summary = "푼 시험 확인", description = "모의고사 시험 현황을 확인합니다.", tags = "모의고사")
    @GetMapping("/mock-exam/solved")
    public ResponseEntity<Solved.MockExam.Response> getMockExamSolvedInfo(@RequestParam Long memberId) {
        return new ResponseEntity<>(examService.getSolvedInfo(memberId), HttpStatus.OK);
    }

    @Operation(summary = "시험 시작", description = "시험을 시작합니다.", tags = "골고루")
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

    @Operation(summary = "채점", description = "답안을 채점합니다.", tags = "모의고사")
    @PostMapping("/mock-exam/grade")
    public ResponseEntity<Grade.Response> grade(@RequestBody Grade.Request request) {
        return new ResponseEntity<>(examService.grade(request), HttpStatus.OK);
    }

    @Operation(summary = "시험 종료", description = "시험을 종료합니다.", tags = {"골고루", "유형별"})
    @PatchMapping("/end/{examId}")
    public ResponseEntity<End.Response> end(@PathVariable Long examId) {
        return new ResponseEntity<>(examService.end(examId), HttpStatus.OK);
    }

}
