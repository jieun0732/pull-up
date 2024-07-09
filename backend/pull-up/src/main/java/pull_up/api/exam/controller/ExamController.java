package pull_up.api.exam.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.exam.service.ExamService;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.global.common.response.BaseResponse;

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

    /**
     * 주어진 카테고리와 entry에 따라 문제 목록을 반환합니다.
     * @param category 문제의 카테고리.
     * @param entry 문제의 entry.
     * @param type 문제의 유형 (선택 사항).
     * @return 문제 목록.
     */
    @GetMapping
    public BaseResponse<List<ProblemDto>> getProblemsByCategoryAndEntry(
        @RequestParam String category,
        @RequestParam(required = false) String entry,
        @RequestParam(required = false) String type
    ) {
        List<ProblemDto> problems = examService.getProblemsByCategoryAndEntry(category, entry, type);
        return BaseResponse.success(HttpStatus.OK.value(), "시험문제를 출력합니다.", problems);
    }

    /**
     * 시험 생성 및 문제 할당.
     * @param memberId 시험을 생성할 멤버의 ID.
     * @param category 시험의 카테고리.
     * @param entry 시험의 entry.
     * @param type 시험의 유형 (선택 사항).
     * @return 생성된 시험 정보.
     */
    @PostMapping("/createExam")
    public BaseResponse<ExamInformationDto> createExam(
        @RequestParam Long memberId,
        @RequestParam String category,
        @RequestParam(required = false) String entry,
        @RequestParam(required = false) String type
    ) {
        ExamInformationDto examInformationDto = examService.createExam(memberId, category, entry, type);
        return BaseResponse.success(HttpStatus.OK.value(), "시험을 생성하였습니다.", examInformationDto);
    }

    /**
     * 모의고사 문제 리스트를 반환합니다.
     * @param memberId 멤버의 ID.
     * @return 모의고사 문제 목록.
     */
    @GetMapping("/mockExam/{memberId}")
    public BaseResponse<List<ProblemDto>> getMockExamProblems(@PathVariable Long memberId) {
        List<ProblemDto> problems = examService.getMockExamProblems(memberId);
        return BaseResponse.success(HttpStatus.OK.value(), "시험문제를 출력합니다.", problems);
    }
}