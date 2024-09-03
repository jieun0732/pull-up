package pull_up.api.exam.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pull_up.api.exam.dto.CreatedExamInformationResultDto;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.exam.dto.ExamProblemResponseDto;
import pull_up.api.exam.dto.ExamProblemResultDto;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.exam.entity.ExamProblem;
import pull_up.api.exam.exception.ExamErrorCode;
import pull_up.api.exam.exception.ExamException;
import pull_up.api.exam.repository.ExamInformationRepository;
import pull_up.api.exam.repository.ExamProblemRepository;
import pull_up.api.member.dto.IncorrectAnswerDto;
import pull_up.api.member.dto.IncorrectAnswerResultDto;
import pull_up.api.member.dto.MemberAnswerDto;
import pull_up.api.member.dto.MemberAnswerIndexDto;
import pull_up.api.member.dto.MemberAnswerResponseDto;
import pull_up.api.member.dto.MemberAnswerResultDto;
import pull_up.api.member.dto.MemberAnswerSolvedDto;
import pull_up.api.member.entity.IncorrectAnswer;
import pull_up.api.member.entity.Member;
import pull_up.api.member.entity.MemberAnswer;
import pull_up.api.member.exception.IncorrectAnswerErrorCode;
import pull_up.api.member.exception.IncorrectAnswerException;
import pull_up.api.member.exception.MemberAnswerErrorCode;
import pull_up.api.member.exception.MemberAnswerException;
import pull_up.api.member.exception.MemberErrorCode;
import pull_up.api.member.exception.MemberException;
import pull_up.api.member.repository.IncorrectAnswerRepository;
import pull_up.api.member.repository.MemberAnswerRepository;
import pull_up.api.member.repository.MemberRepository;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.api.problem.dto.ProblemResultDto;
import pull_up.api.problem.dto.ProblemSolvedDto;
import pull_up.api.problem.dto.ProblemTimeResultDto;
import pull_up.api.problem.dto.ProblemTypeSummaryDto;
import pull_up.api.problem.entity.Problem;
import pull_up.api.problem.exception.ProblemErrorCode;
import pull_up.api.problem.exception.ProblemException;
import pull_up.api.problem.repository.ProblemRepository;

/**
 * 시험 관련 비즈니스 로직을 처리하는 서비스.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    private final ProblemRepository problemRepository;
    private final MemberRepository memberRepository;
    private final MemberAnswerRepository memberAnswerRepository;
    private final IncorrectAnswerRepository incorrectAnswerRepository;
    private final ExamInformationRepository examInformationRepository;
    private final ExamProblemRepository examProblemRepository;


    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 문제 리스트 조회 (골고루 풀기 및 유형별 풀기).
     */
    public List<MemberAnswerResultDto> getProblemList(Long memberId, String entry, String category,
        String type) {
        List<MemberAnswer> memberAnswers = memberAnswerRepository.findByMemberAndOptionalFilters(
            memberId, entry, category, type);
        return memberAnswers.stream().map(MemberAnswerResultDto::from).collect(Collectors.toList());
    }

    /**
     * 문제 index 리스트 조회 ( 골고루 , 유형별 )
     */
    public List<MemberAnswerIndexDto> getProblemIndexList(Long memberId, String entry,
        String category,
        String type) {
        List<MemberAnswer> memberAnswers = memberAnswerRepository.findByMemberAndOptionalFilters(
            memberId, entry, category, type);
        return memberAnswers.stream().map(MemberAnswerIndexDto::from).collect(Collectors.toList());
    }

    /**
     * MemberAnswerId를 이용한 문제 조회.
     */
    public MemberAnswerResultDto getProblemByMemberAnswerId(Long memberAnswerId) {

        return memberAnswerRepository.findById(memberAnswerId)
            .map(MemberAnswerResultDto::from)
            .orElseThrow(() -> new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM));

    }


    /**
     * 문제 요약 정보 조회 메서드들.
     */
    public List<ProblemTypeSummaryDto> getMathProblemsSummary(Long memberId) {
        return getCombinedProblemSummary(memberId, "수리");
    }

    public List<ProblemTypeSummaryDto> getLanguageProblemsSummary(Long memberId) {
        return getCombinedProblemSummary(memberId, "언어");
    }

    public List<ProblemTypeSummaryDto> getReasoningProblemsSummary(Long memberId) {
        return getCombinedProblemSummary(memberId, "추리");
    }

    private List<ProblemTypeSummaryDto> getCombinedProblemSummary(Long memberId, String entry) {
        // 골고루 학습과 유형별에 대해 각각 요약 정보를 조회
        List<ProblemTypeSummaryDto> balancedLearningSummary = getProblemSummaryByEntry(memberId,
            entry, "골고루");
        List<ProblemTypeSummaryDto> typeBasedSummary = getProblemSummaryByEntry(memberId, entry,
            "유형별");

        // 두 요약 정보를 합쳐서 반환
        List<ProblemTypeSummaryDto> combinedSummary = new ArrayList<>();
        combinedSummary.addAll(balancedLearningSummary);
        combinedSummary.addAll(typeBasedSummary);

        return combinedSummary;
    }

    private List<ProblemTypeSummaryDto> getProblemSummaryByEntry(Long memberId, String entry,
        String category) {
        // 모든 문제를 entry와 category에 따라 조회
        List<ProblemDto> problemDtos = problemRepository.findByEntryAndCategory(entry, category);

        // 각 type별로 문제 개수를 세고, 선택된 답변의 개수도 세기
        Map<String, Long> totalProblemsByType = problemDtos.stream()
            .collect(Collectors.groupingBy(ProblemDto::type, Collectors.counting()));

        // 선택된 답변의 개수를 세고, 정답 여부 확인
        Map<String, Long> answeredProblemsByType = new HashMap<>();
        Map<String, Boolean> isCorrectByType = new HashMap<>();

        for (ProblemDto problemDto : problemDtos) {
            boolean hasAnswer =
                memberAnswerRepository.countAnsweredProblemsByMemberAndProblem(memberId,
                    problemDto.id()) > 0;
            boolean isCorrect = memberAnswerRepository.existsByMemberIdAndProblemIdAndIsCorrect(
                memberId, problemDto.id(), true);

            if (hasAnswer) {
                answeredProblemsByType.merge(problemDto.type(), 1L, Long::sum);
                isCorrectByType.merge(problemDto.type(), isCorrect,
                    (oldValue, newValue) -> oldValue && newValue);
            } else {
                isCorrectByType.putIfAbsent(problemDto.type(), true); // 초기값을 true로 설정
            }
        }

        // 각 타입별로 요약 정보 생성
        return totalProblemsByType.entrySet().stream()
            .map(entrySet -> {
                String type = entrySet.getKey();
                Long totalProblems = entrySet.getValue();
                Long answeredProblems = answeredProblemsByType.getOrDefault(type, 0L);
                Boolean isCorrect = isCorrectByType.getOrDefault(type, true);
                return ProblemTypeSummaryDto.of(category, type, totalProblems, answeredProblems,
                    isCorrect);
            })
            .collect(Collectors.toList());
    }

    /**
     * 문제 푼 여부 조회.
     */
    public List<MemberAnswerSolvedDto> getProblemSolvedList(Long memberId, String entry,
        String category, String type) {
        List<MemberAnswer> memberAnswers = memberAnswerRepository.findByMemberAndOptionalFilters(
            memberId, entry, category, type);

        return memberAnswers.stream()
            .map(MemberAnswerSolvedDto::from)
            .collect(Collectors.toList());
    }

    /**
     * 모의고사 문제 푼 여부 조회.
     */
    public List<ProblemSolvedDto> getProblemsSolvedByExamInformation(Long examInformationId) {
        List<ExamProblem> examProblems = examProblemRepository.findByExamInformationId(
            examInformationId);

        return examProblems.stream()
            .map(ProblemSolvedDto::from)
            .collect(Collectors.toList());
    }

    /**
     * 모의고사 문제 목록 조회.
     */
    public List<ProblemSolvedDto> getExamProblemByExamInformation(Long examInformationId) {
        List<ExamProblem> examProblems = examProblemRepository.findByExamInformationId(
            examInformationId);

        return examProblems.stream()
            .map(ProblemSolvedDto::from)
            .collect(Collectors.toList());
    }

    /**
     * 문제 답안 저장하기.
     */
    public MemberAnswerResultDto saveAnswer(MemberAnswerResponseDto memberAnswerResponseDto) {
        MemberAnswer memberAnswer = memberAnswerRepository.findById(memberAnswerResponseDto.id())
            .orElseThrow(() -> new MemberAnswerException(MemberAnswerErrorCode.NOT_FOUND_MEMBERANSWER));

        // 3. 사용자가 제출한 답안을 설정
        memberAnswer.setChosenAnswer(memberAnswerResponseDto.chosenAnswer());

        Problem problem = memberAnswer.getProblem();
        Member member = memberAnswer.getMember();

        // 4. 정답 여부를 판별
        boolean isCorrect = checkAnswer(problem.getId(),
            memberAnswerResponseDto.chosenAnswer());
        memberAnswer.setIsCorrect(isCorrect);

        // 5. MemberAnswer를 저장
        memberAnswerRepository.save(memberAnswer);

        // 6. 오답인 경우, IncorrectAnswer를 기록
        if (!isCorrect) {
            IncorrectAnswer incorrectAnswer = IncorrectAnswer.of(member, problem, null,
                memberAnswerResponseDto.chosenAnswer());
            incorrectAnswerRepository.save(incorrectAnswer);
        }

        problem.setTotalAttempts(problem.getTotalAttempts() + 1);
        if (!isCorrect) {
            problem.setIncorrectAttempts(problem.getIncorrectAttempts() + 1);
        }
        problem.setIncorrectRate((double) problem.getIncorrectAttempts() / problem.getTotalAttempts() * 100);
        problemRepository.save(problem);

        // 7. 결과를 DTO로 변환하여 반환
        return MemberAnswerResultDto.from(memberAnswer);
    }

    /**
     * 문제의 답과 사용의 답 확인하기.
     */
    private boolean checkAnswer(Long problemId, String chosenAnswer) {
        Problem problem = problemRepository.findById(problemId).orElseThrow();
        return problem.getAnswer().equals(chosenAnswer);
    }

    /**
     * 다시 풀기.
     */
    public void resetAnswers(Long memberId, String entry, String category, String type) {
        List<MemberAnswer> memberAnswers;
        if (type == null || type.isEmpty()) {
            memberAnswers = memberAnswerRepository.findByMemberIdAndProblemEntryAndProblemCategory(
                memberId, entry, category);
        } else {
            memberAnswers = memberAnswerRepository.findByMemberIdAndProblemEntryAndProblemCategoryAndProblemType(
                memberId, entry, category, type);
        }
        for (MemberAnswer answer : memberAnswers) {
            answer.setChosenAnswer(null);
            answer.setIsCorrect(null);
        }
        memberAnswerRepository.saveAll(memberAnswers);
    }

    /**
     * 이어 풀기.
     */
    public MemberAnswerResultDto getNextUnanswered(Long memberId, String entry, String category,
        String type) {
        List<MemberAnswer> memberAnswers;
        if (type == null || type.isEmpty()) {
            memberAnswers = memberAnswerRepository.findByMemberIdAndProblemEntryAndProblemCategory(
                memberId, entry, category);
        } else {
            memberAnswers = memberAnswerRepository.findByMemberIdAndProblemEntryAndProblemCategoryAndProblemType(
                memberId, entry, category, type);
        }
        for (MemberAnswer answer : memberAnswers) {
            if (answer.getChosenAnswer() == null) {
                return MemberAnswerResultDto.from(answer);
            }
        }
        return null;
    }

    /**
     * 모의고사 문제 리스트 조회.
     */
    public List<ProblemResultDto> getMockExamProblems() {
        List<Problem> problems = problemRepository.findByCategory("모의고사");
        Collections.shuffle(problems);
        return problems.stream().limit(20).map(ProblemResultDto::from).collect(Collectors.toList());
    }

    /**
     * 모의고사 시작하기.
     */
    public CreatedExamInformationResultDto startMockExam(
        Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        ExamInformation examInformation = ExamInformation.of(
            member,
            null,
            "모의고사",
            null,
            LocalDateTime.now(),
            null,
            null,
            0
        );

        examInformationRepository.save(examInformation);

        // 각 entry에서 선택할 문제 수 설정
        Map<String, Integer> entryLimits = Map.of(
            "수리", 6,
            "언어", 7,
            "추리", 7
        );

        // 각 entry에서 문제를 랜덤으로 선택할 리스트
        List<Problem> selectedProblems = new ArrayList<>();

        // 각 entry별로 문제를 선택
        for (Map.Entry<String, Integer> entry : entryLimits.entrySet()) {
            String entryName = entry.getKey();
            int limit = entry.getValue();
            log.info("entry: " + entryName);
            log.info("limit: " + limit);

            // "모의고사" 카테고리와 entry별로 문제를 필터링
            List<Problem> problems = problemRepository.findByCategoryAndEntry("모의고사", entryName);
            log.info("problems1: " + problems);

            // 문제를 랜덤으로 섞음
            Collections.shuffle(problems);

            // 지정된 개수만큼 문제를 선택
            List<Problem> chosenProblems = problems.stream()
                .limit(limit)
                .toList();
            log.info("problems2: " + chosenProblems);

            // 선택된 문제를 리스트에 추가
            selectedProblems.addAll(chosenProblems);
        }

        // 선택된 문제를 기반으로 ExamProblem 객체 생성
        List<ExamProblem> examProblems = new ArrayList<>();
        Long problemNumber = 1L; // 문제 번호를 1부터 시작

        for (Problem problem : selectedProblems) {
            ExamProblem examProblem = ExamProblem.of(
                examInformation,
                problem,
                problemNumber, // 문제 번호를 추가
                null, // 선택 답변은 나중에 설정
                null
            );
            log.info("examProblem3: " + examProblem);
            examProblems.add(examProblem);
            log.info("examProblem4: " + examProblems);
            problemNumber++; // 다음 문제의 번호를 증가
        }

        // ExamProblem 객체를 저장
        examProblemRepository.saveAll(examProblems);

        // ExamProblemDto 리스트 생성
        List<ExamProblemResultDto> examProblemResultDtos = examProblems.stream()
            .map(ExamProblemResultDto::from)
            .collect(Collectors.toList());
        log.info("examProblems5: " + examProblemResultDtos);

        // 결과를 반환
        return CreatedExamInformationResultDto.from(examInformation, examProblemResultDtos);
    }


    /**
     * 문제 ID를 통해 문제를 반환합니다.
     */
    public ProblemTimeResultDto getProblemByExamProblemId(Long examProblemId) {
        // ExamProblem 엔티티를 찾음
        ExamProblem examProblem = examProblemRepository.findById(examProblemId)
            .orElseThrow(() -> new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM));

        // ExamProblem에서 Problem을 추출하여 반환
        Problem problem = examProblem.getProblem();
        LocalDateTime createdDate = examProblem.getExamInformation()
            .getCreatedDate(); // createdDate 가져오기
        return ProblemTimeResultDto.from(problem, createdDate);
    }

    /**
     * 모의고사 ID 및 문제 번호를 통해 문제를 반환합니다.
     */
    public ProblemTimeResultDto getProblemByExamInformationIdAndProblemNumber(
        Long examInformationId, Long problemNumber) {
        ExamProblem examProblem = examProblemRepository.findByExamInformationIdAndProblemNumber(
            examInformationId, problemNumber);
        if (examProblem == null) {
            throw new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM);
        }
        Problem problem = examProblem.getProblem();
        LocalDateTime createdDate = examProblem.getExamInformation()
            .getCreatedDate(); // createdDate 가져오기
        return ProblemTimeResultDto.from(problem, createdDate); // createdDate 함께 전달
    }

    /**
     * 모의고사 답안 저장하기.
     */
    public ExamProblemResultDto saveMockExamAnswer(ExamProblemResponseDto examProblemResponseDto) {
        // 1. ExamProblem을 찾음
        ExamProblem examProblem = examProblemRepository.findById(
                examProblemResponseDto.examProblemId())
            .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM_PROBLEM));

        // 2. 사용자가 제출한 답안을 설정
        examProblem.setChosenAnswer(examProblemResponseDto.chosenAnswer());

        // 3. 문제를 찾음
        Problem problem = examProblem.getProblem();

        // 4. 정답 여부를 판별
        boolean isCorrect = checkAnswer(problem.getId(), examProblemResponseDto.chosenAnswer());
        examProblem.setIsCorrect(isCorrect);

        // 5. ExamProblem을 저장
        examProblemRepository.save(examProblem);

        // 6. 오답인 경우, IncorrectAnswer를 기록
        if (!isCorrect) {
            Member member = examProblem.getExamInformation().getMember();
            IncorrectAnswer incorrectAnswer = IncorrectAnswer.of(member, problem,
                examProblem.getExamInformation(), examProblemResponseDto.chosenAnswer());
            incorrectAnswerRepository.save(incorrectAnswer);
        }

        // 7. 결과를 DTO로 변환하여 반환
        return ExamProblemResultDto.from(examProblem);
    }

    /**
     * 모의고사 완료 및 점수 저장하기.
     */
    public ExamInformationDto completeMockExam(ExamInformationDto examInformationDto) {
        ExamInformation examInformation = examInformationRepository.findById(
                examInformationDto.id())
            .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));
        examInformation.setSolvedDate(LocalDateTime.now());

        List<ExamProblem> answers = examProblemRepository.findByExamInformationId(
            examInformationDto.id());
        int score = (int) answers.stream().filter(ExamProblem::getIsCorrect).count() * 5;
        examInformation.setScore(score);
        examInformation.setRequiredTime(
            Duration.between(examInformation.getCreatedDate(), examInformation.getSolvedDate()));

        examInformationRepository.save(examInformation);
        return ExamInformationDto.from(examInformation);
    }

    /**
     * 틀린 문제 리스트 조회하기.
     */
    public List<IncorrectAnswerResultDto> getIncorrectAnswers(Long memberId) {
        List<IncorrectAnswer> incorrectAnswers = incorrectAnswerRepository.findByMemberId(memberId);
        if (incorrectAnswers == null || incorrectAnswers.isEmpty()) {
            return null;
        }
        return incorrectAnswers.stream().map(IncorrectAnswerResultDto::from).collect(Collectors.toList());
    }

    /**
     * 틀린 문제 상세 조회하기.
     */
    public IncorrectAnswerResultDto getIncorrectAnswer(Long id) {
        IncorrectAnswer incorrectAnswer = incorrectAnswerRepository.findById(id)
            .orElseThrow(() -> new IncorrectAnswerException(
                IncorrectAnswerErrorCode.NOT_FOUND_INCORRECT_ANSWER));
        return IncorrectAnswerResultDto.from(incorrectAnswer);
    }
}
