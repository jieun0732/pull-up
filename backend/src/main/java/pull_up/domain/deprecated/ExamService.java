package pull_up.domain.deprecated;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pull_up.api.exam.dto.IncorrectAnswer;
import pull_up.api.exam.dto.*;
import pull_up.domain.exam.exception.ExamErrorCode;
import pull_up.domain.exam.exception.ExamException;
import pull_up.api.member.dto.*;
import pull_up.api.problem.dto.*;
import pull_up.global.dto.ListDto;
import pull_up.global.entity.BaseEntity;
import pull_up.domain.member.member.MemberErrorCode;
import pull_up.domain.member.member.MemberException;
import pull_up.domain.problem.problem.ProblemErrorCode;
import pull_up.domain.problem.problem.ProblemException;
import pull_up.infra.database.entity.legacy.*;
import pull_up.infra.database.repository.answer.AnswerRepositoryL;
import pull_up.infra.database.repository.exam.ExamRepositoryL;
import pull_up.infra.database.repository.legacy.IncorrectAnswerRepository;
import pull_up.infra.database.repository.legacy.MemberAnswerRepository;
import pull_up.infra.database.repository.member.MemberRepositoryL;
import pull_up.infra.database.repository.problem.ProblemRepositoryL;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static pull_up.domain.exam.exception.ExamErrorCode.NOT_FOUND_EXAM;

/**
 * 시험 관련 비즈니스 로직을 처리하는 서비스.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ExamService {

    private final ProblemRepositoryL problemRepository;
    private final MemberRepositoryL memberRepositoryL;
    private final MemberAnswerRepository memberAnswerRepository;
    private final IncorrectAnswerRepository incorrectAnswerRepository;
    private final ExamRepositoryL examRepositoryL;
    private final AnswerRepositoryL answerRepositoryL;

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
            boolean isCorrect = memberAnswerRepository.existsByMemberLIdAndProblemLIdAndIsCorrect(
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
        List<AnswerL> answerLS = answerRepositoryL.findByExamLId(
                examInformationId);

        return answerLS.stream()
                .map(ProblemSolvedDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 모의고사 문제 목록 조회.
     */
    public List<ProblemSolvedDto> getExamProblemByExamInformation(Long examInformationId) {
        List<AnswerL> answerLS = answerRepositoryL.findByExamLId(
                examInformationId);

        return answerLS.stream()
                .map(ProblemSolvedDto::from)
                .collect(Collectors.toList());
    }


    /**
     * 다시 풀기.
     */
    public void resetAnswers(Long memberId, String entry, String category, String type) {
        List<MemberAnswer> memberAnswers;
        if (type == null || type.isEmpty()) {
            memberAnswers = memberAnswerRepository.findByMemberLIdAndProblemLEntryAndProblemLCategory(
                    memberId, entry, category);
        } else {
            memberAnswers = memberAnswerRepository.findByMemberLIdAndProblemLEntryAndProblemLCategoryAndProblemLType(
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
            memberAnswers = memberAnswerRepository.findByMemberLIdAndProblemLEntryAndProblemLCategory(
                    memberId, entry, category);
        } else {
            memberAnswers = memberAnswerRepository.findByMemberLIdAndProblemLEntryAndProblemLCategoryAndProblemLType(
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
        List<ProblemL> problemLS = problemRepository.findByCategory("모의고사");
        Collections.shuffle(problemLS);
        return problemLS.stream().limit(20).map(ProblemResultDto::from).collect(Collectors.toList());
    }

    /**
     * 모의고사 시작하기.
     */
    public CreatedExamInformationResultDto startMockExam(
            Long memberId) {
        MemberL memberL = memberRepositoryL.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        ExamL examL = ExamL.of(
                memberL,
                null,
                "모의고사",
                null,
                LocalDateTime.now(),
                null,
                null,
                0
        );

        examRepositoryL.save(examL);

        // 각 entry에서 선택할 문제 수 설정
        Map<String, Integer> entryLimits = Map.of(
                "수리", 7,
                "언어", 7,
                "추리", 6
        );

        // 각 entry에서 문제를 랜덤으로 선택할 리스트
        List<ProblemL> selectedProblemLS = new ArrayList<>();

        // 각 entry별로 문제를 선택
        for (Map.Entry<String, Integer> entry : entryLimits.entrySet()) {
            String entryName = entry.getKey();
            int limit = entry.getValue();
            log.info("entry: " + entryName);
            log.info("limit: " + limit);

            // "모의고사" 카테고리와 entry별로 문제를 필터링
            List<ProblemL> problemLS = problemRepository.findByCategoryAndEntry("모의고사", entryName);
            log.info("problems1: " + problemLS);

            // 문제를 랜덤으로 섞음
            Collections.shuffle(problemLS);

            // 지정된 개수만큼 문제를 선택
            List<ProblemL> chosenProblemLS = problemLS.stream()
                    .limit(limit)
                    .toList();
            log.info("problems2: " + chosenProblemLS);

            // 선택된 문제를 리스트에 추가
            selectedProblemLS.addAll(chosenProblemLS);
        }

        // 선택된 문제를 기반으로 ExamProblem 객체 생성
        List<AnswerL> answerLS = new ArrayList<>();
        Long problemNumber = 1L; // 문제 번호를 1부터 시작

        for (ProblemL problemL : selectedProblemLS) {
            AnswerL answerL = AnswerL.of(
                    examL,
                    problemL,
                    problemNumber, // 문제 번호를 추가
                    null, // 선택 답변은 나중에 설정
                    false
            );

            answerLS.add(answerL);
            problemNumber++; // 다음 문제의 번호를 증가
        }

        // ExamProblem 객체를 저장
        answerRepositoryL.saveAll(answerLS);

        // ExamProblemDto 리스트 생성
        List<ExamProblemResultDto> examProblemResultDtos = answerLS.stream()
                .map(ExamProblemResultDto::from)
                .collect(Collectors.toList());

        // 결과를 반환
        return CreatedExamInformationResultDto.from(examL, examProblemResultDtos);
    }


    /**
     * 문제 ID를 통해 문제를 반환합니다.
     */
    public ProblemTimeResultDto getProblemByExamProblemId(Long examProblemId) {
        // ExamProblem 엔티티를 찾음
        AnswerL answerL = answerRepositoryL.findById(examProblemId)
                .orElseThrow(() -> new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM));

        // ExamProblem에서 Problem을 추출하여 반환
        ProblemL problemL = answerL.getProblemL();
        LocalDateTime createdDate = answerL.getExamL()
                .getCreatedDate();
        String chosenAnswer = answerL.getChosenAnswer();
        return ProblemTimeResultDto.from(problemL, createdDate, answerL.getProblemNumber(),
                chosenAnswer);
    }

    /**
     * 모의고사 ID 및 문제 번호를 통해 문제를 반환합니다.
     */
    public ProblemTimeResultDto getProblemByExamInformationIdAndProblemNumber(
            Long examInformationId, Long problemNumber) {
        AnswerL answerL = answerRepositoryL.findByExamLIdAndProblemNumber(
                examInformationId, problemNumber);
        if (answerL == null) {
            throw new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM);
        }
        ProblemL problemL = answerL.getProblemL();
        LocalDateTime createdDate = answerL.getExamL()
                .getCreatedDate();
        String chosenAnswer = answerL.getChosenAnswer();
        return ProblemTimeResultDto.from(problemL, createdDate, answerL.getProblemNumber(),
                chosenAnswer); // createdDate 함께 전달
    }

    /**
     * 모의고사 답안 저장하기.
     */
    public ExamProblemResultDto saveMockExamAnswer(ExamProblemResponseDto examProblemResponseDto) {
        // 1. ExamProblem을 찾음
        AnswerL answerL = answerRepositoryL.findByExamLIdAndProblemNumber(
                examProblemResponseDto.examInformationId(), examProblemResponseDto.problemNumber());

        // 2. 사용자가 제출한 답안을 설정
        answerL.setChosenAnswer(examProblemResponseDto.chosenAnswer());

        // 3. 문제를 찾음
        ProblemL problemL = answerL.getProblemL();

        // 4. 정답 여부를 판별
        boolean isCorrect = checkAnswer(problemL.getId(), examProblemResponseDto.chosenAnswer());
        answerL.setIsCorrect(isCorrect);

        // 5. ExamProblem을 저장
        answerRepositoryL.save(answerL);

        // 6. IncorrectAnswer 처리
        MemberL memberL = answerL.getExamL().getMemberL();
        Optional<pull_up.infra.database.entity.legacy.IncorrectAnswer> existingIncorrectAnswer = incorrectAnswerRepository.findByMemberLAndProblemLAndExamL(
                memberL, problemL, answerL.getExamL());

        if (isCorrect) {
            // 정답일 경우 기존 오답 기록이 있으면 삭제
            existingIncorrectAnswer.ifPresent(BaseEntity::softDelete);
        } else {
            if (existingIncorrectAnswer.isPresent()) {
                // 오답일 경우 기존 오답 기록이 있으면 LocalDateTime 업데이트
                pull_up.infra.database.entity.legacy.IncorrectAnswer incorrectAnswer = existingIncorrectAnswer.get();
                incorrectAnswer.setIncorrectTime(LocalDateTime.now());
                incorrectAnswer.setChosenAnswer(examProblemResponseDto.chosenAnswer());
                incorrectAnswerRepository.save(incorrectAnswer);
            } else {
                // 오답일 경우 기존 오답 기록이 없으면 새로 저장
                pull_up.infra.database.entity.legacy.IncorrectAnswer incorrectAnswer = pull_up.infra.database.entity.legacy.IncorrectAnswer.of(memberL, problemL,
                        answerL.getExamL(), examProblemResponseDto.chosenAnswer(),
                        LocalDateTime.now());
                incorrectAnswerRepository.save(incorrectAnswer);
            }
        }

        problemL.addTotalAttempt(isCorrect);

        // 7. 결과를 DTO로 변환하여 반환
        return ExamProblemResultDto.from(answerL);
    }

    /**
     * 모의고사 완료 및 점수 저장하기.
     */
    public ExamInformationDto completeMockExam(Long examInformationId) {
        ExamL examL = examRepositoryL.findById(
                        examInformationId)
                .orElseThrow(() -> new ExamException(NOT_FOUND_EXAM));
        examL.setSolvedTime(LocalDateTime.now());

        List<AnswerL> answerLS = answerRepositoryL.findByExamLId(
                examInformationId);
        int score = (int) answerLS.stream().filter(AnswerL::getIsCorrect).count() * 5;
        examL.setScore(score);
        examL.setRequiredTime(
                Duration.between(examL.getCreatedDate(), examL.getSolvedTime()));

        examRepositoryL.save(examL);
        return ExamInformationDto.from(examL);
    }

    /**
     * 문제의 답과 사용의 답 확인하기.
     */
    private boolean checkAnswer(Long problemId, String chosenAnswer) {
        ProblemL problemL = problemRepository.findById(problemId).orElseThrow();
        return problemL.getAnswer().equals(chosenAnswer);
    }

    /**
     * 틀린 문제 리스트 조회하기.
     */
    public ListDto<IncorrectAnswer.Brief> getIncorrectAnswers(Long memberId) {
        List<AnswerL> incorrectAnswerLS = answerRepositoryL.findIncorrectAnswersByMemberId(memberId);
        return new ListDto<>(incorrectAnswerLS.stream().map(IncorrectAnswer.Brief::toDto).toList());
    }

    /**
     * 틀린 문제 상세 조회하기.
     */
    public IncorrectAnswer.Detail getIncorrectAnswerDetail(Long answerId) {
        AnswerL incorrectAnswerL = answerRepositoryL.findByIdWithProblem(answerId)
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM_PROBLEM));
        return IncorrectAnswer.Detail.toDto(incorrectAnswerL);
    }

    /**
     * 전체 모의고사의 평균 점수 및 평균 소요 시간 구하기.
     */
    public ExamInformationAverageScoreDto calculateAverageScore() {
        List<ExamL> examLS = examRepositoryL.findAll();

        if (examLS.isEmpty()) {
            return ExamInformationAverageScoreDto.of(0.0,
                    Duration.ZERO); // 조회된 데이터가 없을 경우, 평균 점수는 0
        }

        Duration totalDuration = examLS.stream()
                .map(ExamL::getRequiredTime)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);

        double totalScore = examLS.stream()
                .mapToInt(ExamL::getScore)
                .sum();

        double averageScore = totalScore / examLS.size();
        return ExamInformationAverageScoreDto.of(averageScore, totalDuration);
    }

    /**
     * 모의고사 문제 유형별로 총 문제 수와 맞힌 문제 수를 반환합니다.
     */
    public List<ProblemTypeResultDto> getProblemTypeResults(Long examInformationId) {
        ExamL examL = examRepositoryL.findById(examInformationId)
                .orElseThrow(() -> new ExamException(NOT_FOUND_EXAM));

        List<AnswerL> answerLS = answerRepositoryL.findByExamLId(
                examInformationId);

        Map<String, List<AnswerL>> groupedByEntry = answerLS.stream()
                .collect(Collectors.groupingBy(examProblem -> examProblem.getProblemL().getEntry()));

        return groupedByEntry.entrySet().stream()
                .map(entry -> {
                    String entryName = entry.getKey();
                    List<AnswerL> problems = entry.getValue();
                    int totalProblems = problems.size();
                    int correctProblems = (int) problems.stream().filter(AnswerL::getIsCorrect)
                            .count();

                    return ProblemTypeResultDto.of(entryName, totalProblems, correctProblems);
                })
                .collect(Collectors.toList());
    }

    /**
     * 멤버의 가장 최근 ExamInformation 가져오기.
     */
    public ExamInformationDetailDto getRecentExamInformation(Long memberId) {
        // 가장 최근 ExamInformation을 가져옴
        ExamL recentExamL = examRepositoryL.findTopByMemberLIdOrderByCreatedDateDesc(
                        memberId)
                .orElseThrow(() -> new ExamException(NOT_FOUND_EXAM));

        // ExamInformationId로 관련된 ExamProblems 조회
        List<AnswerL> answerLS = answerRepositoryL.findByExamLId(
                recentExamL.getId());

        // 문제들을 엔트리별로 그룹화
        Map<String, List<AnswerL>> groupedByEntry = answerLS.stream()
                .collect(Collectors.groupingBy(examProblem -> examProblem.getProblemL().getEntry()));

        // 각 엔트리별로 문제 수와 정답 수 계산
        List<ProblemTypeResultDto> problemTypeResults = groupedByEntry.entrySet().stream()
                .map(entry -> {
                    String entryName = entry.getKey();
                    List<AnswerL> problems = entry.getValue();
                    int totalProblems = problems.size();
                    int correctProblems = (int) problems.stream()
                            .filter(examProblem -> Boolean.TRUE.equals(
                                    examProblem.getIsCorrect())) // isCorrect가 true인 경우만 필터링
                            .count();

                    return ProblemTypeResultDto.of(entryName, totalProblems, correctProblems);
                })
                .collect(Collectors.toList());

        // 전체 정답 수 계산
        int totalCorrectAnswers = (int) answerLS.stream()
                .filter(examProblem -> Boolean.TRUE.equals(
                        examProblem.getIsCorrect())) // isCorrect가 true인 경우만 필터링
                .count();

        // 랭크 퍼센트 계산
        String rankPercent = calculateRankPercent(totalCorrectAnswers);

        // 모든 시험 정보 조회 및 평균 점수, 평균 소요 시간 계산
        List<ExamL> allExamLS = examRepositoryL.findAll();

        OptionalDouble averageScoreOpt = allExamLS.stream()
                .mapToInt(ExamL::getScore)
                .average();

        double averageScore = averageScoreOpt.orElse(0.0);

        Duration totalDuration = allExamLS.stream()
                .map(ExamL::getRequiredTime)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);

        Duration averageTime =
                totalDuration.isZero() ? null : totalDuration.dividedBy(allExamLS.size());

        // 결과 반환
        return ExamInformationDetailDto.of(
                recentExamL.getId(),
                recentExamL.getCreatedDate(),
                recentExamL.getSolvedTime(),
                recentExamL.getRequiredTime(),
                recentExamL.getScore(),
                problemTypeResults,
                averageScore,
                averageTime,
                totalCorrectAnswers,
                rankPercent
        );
    }

    /**
     * 맞힌 문제 수에 따라 상위 퍼센트 계산.
     */
    private String calculateRankPercent(int correctAnswers) {
        Map<Integer, String> rankMap = Map.ofEntries(
                Map.entry(20, "상위 3%"),
                Map.entry(19, "상위 7%"),
                Map.entry(18, "상위 11%"),
                Map.entry(17, "상위 15%"),
                Map.entry(16, "상위 18%"),
                Map.entry(15, "상위 21%"),
                Map.entry(14, "상위 24%"),
                Map.entry(13, "상위 29%"),
                Map.entry(12, "상위 31%"),
                Map.entry(11, "상위 35%"),
                Map.entry(10, "상위 39%"),
                Map.entry(9, "상위 44%"),
                Map.entry(8, "상위 50%"),
                Map.entry(7, "상위 56%"),
                Map.entry(6, "상위 63%"),
                Map.entry(5, "상위 68%"),
                Map.entry(4, "상위 74%"),
                Map.entry(3, "상위 82%"),
                Map.entry(2, "상위 88%"),
                Map.entry(1, "상위 91%"),
                Map.entry(0, "상위 96%")
        );

        return rankMap.getOrDefault(correctAnswers, "순위 정보 없음");
    }

    /**
     * 모의고사 삭제.
     */
    public void deleteMockExam(Long examInformationId) {
        ExamL examL = examRepositoryL.findById(examInformationId)
                .orElseThrow(() -> new ExamException(NOT_FOUND_EXAM));

        List<AnswerL> answerLS = answerRepositoryL.findByExamLId(
                examInformationId);
        answerRepositoryL.deleteAll(answerLS);
        examRepositoryL.delete(examL);
    }

    public ExamGrade.Response grade(ExamGrade.Request request) {
        ExamL examL = examRepositoryL.findByIdWithAnswer(request.examId())
                .orElseThrow(() -> new ExamException(NOT_FOUND_EXAM));

        int[] result = examL.grade(request.getSubmitMap());
        int total = result[0] + result[1];

        return new ExamGrade.Response(total, result[0], result[1], (double) result[0] / total);
    }
}
