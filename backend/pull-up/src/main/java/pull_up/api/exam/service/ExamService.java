package pull_up.api.exam.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.exam.entity.ExamProblem;
import pull_up.api.exam.repository.ExamInformationRepository;
import pull_up.api.exam.repository.ExamProblemRepository;
import pull_up.api.member.entity.Member;
import pull_up.api.member.exception.MemberErrorCode;
import pull_up.api.member.exception.MemberException;
import pull_up.api.member.repository.MemberRepository;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.api.problem.entity.Problem;
import pull_up.api.problem.repository.ProblemRepository;

/**
 * 시험 관련 비즈니스 로직을 처리하는 서비스.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    @Autowired
    private ExamInformationRepository examInformationRepository;

    @Autowired
    private ExamProblemRepository examProblemRepository;

    private static ProblemRepository problemRepository;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 주어진 카테고리와 entry에 따라 문제 목록을 반환합니다.
     * 카테고리가 "골고루 학습"인 경우 entry에 따라 문제를 찾고,
     * "유형별 학습"인 경우 entry와 유형에 따라 문제를 찾습니다.
     * @param category 문제의 카테고리.
     * @param entry 문제의 entry.
     * @param type 문제의 유형 (선택 사항).
     * @return 문제 목록.
     */
    public List<ProblemDto> getProblemsByCategoryAndEntry(String category, String entry, String type) {
        List<Problem> problems;
        if ("골고루 학습".equals(category)) {
            problems = problemRepository.findByCategoryAndEntry(category, entry);
        } else if ("유형별 학습".equals(category)) {
            problems = problemRepository.findByCategoryAndEntryAndType(category, entry, type);
        } else {
            throw new IllegalArgumentException("잘못된 카테고리입니다.");
        }

        return problems.stream().map(ProblemDto::from).collect(Collectors.toList());
    }

    /**
     * 시험을 생성하고 문제를 할당합니다.
     * @param memberId 시험을 생성할 멤버의 ID.
     * @param category 시험의 카테고리.
     * @param entry 시험의 entry.
     * @param type 시험의 유형 (선택 사항).
     * @return 생성된 시험 정보.
     */
    public ExamInformationDto createExam(Long memberId, String category, String entry, String type) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        LocalDateTime now = LocalDateTime.now();
        ExamInformation examInformation = ExamInformation.of(member, category, entry, type, now, now, null);
        examInformation = examInformationRepository.save(examInformation);

        List<Problem> problems;
        if ("골고루 학습".equals(category)) {
            problems = problemRepository.findByCategoryAndEntry(category, entry);
        } else if ("유형별 학습".equals(category)) {
            problems = problemRepository.findByCategoryAndEntryAndType(category, entry, type);
        } else {
            throw new IllegalArgumentException("잘못된 카테고리입니다.");
        }

        for (Problem problem : problems) {
            ExamProblem examProblem = ExamProblem.of(examInformation, problem);
            examProblemRepository.save(examProblem);
        }

        return ExamInformationDto.from(examInformation);
    }

    /**
     * 멤버의 ID를 받아 모의고사 문제 목록을 반환합니다.
     * @param memberId 멤버의 ID.
     * @return 모의고사 문제 목록.
     */
    public List<ProblemDto> getMockExamProblems(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        List<Problem> problems = problemRepository.findByCategory("모의고사");
        return problems.stream().map(ProblemDto::from).collect(Collectors.toList());
    }
}
