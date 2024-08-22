package pull_up.api.exam.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pull_up.api.exam.dto.CreatedExamInformationResponseDto;
import pull_up.api.exam.dto.CreatedExamInformationResultDto;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.exam.dto.ExamProblemDto;
import pull_up.api.exam.dto.ExamProblemResultDto;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.exam.entity.ExamProblem;
import pull_up.api.exam.exception.ExamErrorCode;
import pull_up.api.exam.exception.ExamException;
import pull_up.api.exam.repository.ExamInformationRepository;
import pull_up.api.exam.repository.ExamProblemRepository;
import pull_up.api.member.dto.IncorrectAnswerDto;
import pull_up.api.member.dto.MemberAnswerDto;
import pull_up.api.member.entity.IncorrectAnswer;
import pull_up.api.member.entity.Member;
import pull_up.api.member.entity.MemberAnswer;
import pull_up.api.member.exception.IncorrectAnswerErrorCode;
import pull_up.api.member.exception.IncorrectAnswerException;
import pull_up.api.member.exception.MemberErrorCode;
import pull_up.api.member.exception.MemberException;
import pull_up.api.member.repository.IncorrectAnswerRepository;
import pull_up.api.member.repository.MemberAnswerRepository;
import pull_up.api.member.repository.MemberRepository;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.api.problem.dto.ProblemResultDto;
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

    /**
     * 문제 리스트 조회 (골고루 풀기 및 유형별 풀기)
     */
    public List<MemberAnswerDto> getProblemList(Long memberId, String entry, String category, String type) {
        List<MemberAnswer> memberAnswers;
        if (type == null || type.isEmpty()) {
            memberAnswers = memberAnswerRepository.findByMemberIdAndProblemEntryAndProblemCategory(memberId, entry, category);
        } else {
            memberAnswers = memberAnswerRepository.findByMemberIdAndProblemEntryAndProblemCategoryAndProblemType(memberId, entry, category, type);
        }
        return memberAnswers.stream().map(MemberAnswerDto::from).collect(Collectors.toList());
    }

    /**
     * 문제 답안 저장하기
     */
    public MemberAnswerDto saveAnswer(MemberAnswerDto memberAnswerDto) {
        Member member = memberRepository.findById(memberAnswerDto.member().id())
            .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        Problem problem = problemRepository.findById(memberAnswerDto.problem().id())
            .orElseThrow(() -> new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM));

        MemberAnswer memberAnswer = memberAnswerRepository.findByMemberAndProblem(member, problem)
            .orElseGet(() -> MemberAnswer.of(member, problem, null, null, null));

        memberAnswer.setChosenAnswer(memberAnswerDto.chosenAnswer());

        boolean isCorrect = checkAnswer(memberAnswerDto.problem().id(), memberAnswerDto.chosenAnswer());
        memberAnswer.setIsCorrect(isCorrect);

        memberAnswerRepository.save(memberAnswer);

        if (!isCorrect) {
            IncorrectAnswer incorrectAnswer = IncorrectAnswer.of(member, problem, null, memberAnswerDto.chosenAnswer());
            incorrectAnswerRepository.save(incorrectAnswer);
        }

        return MemberAnswerDto.from(memberAnswer);
    }

    private boolean checkAnswer(Long problemId, String chosenAnswer) {
        Problem problem = problemRepository.findById(problemId).orElseThrow();
        return problem.getAnswer().equals(chosenAnswer);
    }

    /**
     * 다시 풀기
     */
    public void resetAnswers(Long memberId, String entry, String category, String type) {
        List<MemberAnswer> memberAnswers;
        if (type == null || type.isEmpty()) {
            memberAnswers = memberAnswerRepository.findByMemberIdAndProblemEntryAndProblemCategory(memberId, entry, category);
        } else {
            memberAnswers = memberAnswerRepository.findByMemberIdAndProblemEntryAndProblemCategoryAndProblemType(memberId, entry, category, type);
        }
        for (MemberAnswer answer : memberAnswers) {
            answer.setChosenAnswer(null);
            answer.setIsCorrect(null);
        }
        memberAnswerRepository.saveAll(memberAnswers);
    }

    /**
     * 이어 풀기
     */
    public MemberAnswerDto getNextUnanswered(Long memberId, String entry, String category, String type) {
        List<MemberAnswer> memberAnswers;
        if (type == null || type.isEmpty()) {
            memberAnswers = memberAnswerRepository.findByMemberIdAndProblemEntryAndProblemCategory(memberId, entry, category);
        } else {
            memberAnswers = memberAnswerRepository.findByMemberIdAndProblemEntryAndProblemCategoryAndProblemType(memberId, entry, category, type);
        }
        for (MemberAnswer answer : memberAnswers) {
            if (answer.getChosenAnswer() == null) {
                return MemberAnswerDto.from(answer);
            }
        }
        return null;
    }

    /**
     * 모의고사 문제 리스트 조회
     */
    public List<ProblemResultDto> getMockExamProblems() {
        List<Problem> problems = problemRepository.findByCategory("모의고사");
        Collections.shuffle(problems);
        return problems.stream().limit(20).map(ProblemResultDto::from).collect(Collectors.toList());
    }

    /**
     * 모의고사 시작하기
     */
    public CreatedExamInformationResultDto startMockExam(
        CreatedExamInformationResponseDto createdExamInformationDto) {
        Member member = memberRepository.findById(createdExamInformationDto.member().id())
            .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        ExamInformation examInformation = ExamInformation.of(
            member,
            createdExamInformationDto.entry(),
            createdExamInformationDto.category(),
            createdExamInformationDto.type(),
            LocalDateTime.now(),
            null,
            null,
            0
        );

        examInformationRepository.save(examInformation);

        // 문제들을 랜덤으로 선택하여 저장
        List<Problem> problems = problemRepository.findByCategory("모의고사");

        Collections.shuffle(problems); // 문제를 랜덤으로 섞음
        List<Problem> selectedProblems = problems.stream()
            .limit(20) // 20개의 문제를 선택
            .toList();

        List<ExamProblem> examProblems = selectedProblems.stream()
            .map(problem -> ExamProblem.of(
                examInformation,
                problem,
                null, // 선택 답변은 나중에 설정
                null
            ))
            .collect(Collectors.toList());

        examProblemRepository.saveAll(examProblems);

        // ExamProblemDto 리스트 생성
        List<ExamProblemResultDto> examProblemResultDtos = examProblems.stream()
            .map(ExamProblemResultDto::from)
            .collect(Collectors.toList());

        return CreatedExamInformationResultDto.from(examInformation, examProblemResultDtos);
    }


    /**
     * 문제 ID를 통해 문제를 반환합니다.
     */
    public ProblemResultDto getProblemByExamProblemId(Long examProblemId) {
        // ExamProblem 엔티티를 찾음
        ExamProblem examProblem = examProblemRepository.findById(examProblemId)
            .orElseThrow(() -> new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM));

        // ExamProblem에서 Problem을 추출하여 반환
        Problem problem = examProblem.getProblem();
        return ProblemResultDto.from(problem);
    }

    /**
     * 모의고사 답안 저장하기
     */
    public ExamProblemResultDto saveMockExamAnswer(ExamProblemDto examProblemDto) {
        Member member = memberRepository.findById(examProblemDto.examInformation().member().id())
            .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        Problem problem = problemRepository.findById(examProblemDto.problem().id())
            .orElseThrow(() -> new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM));

        ExamInformation examInformation = examInformationRepository.findById(examProblemDto.examInformation().id())
            .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));

        ExamProblem examProblem = examProblemRepository.findByExamInformationAndProblem(examInformation, problem)
            .orElseGet(() -> ExamProblem.of(examInformation, problem, null, null));

        examProblem.setChosenAnswer(examProblemDto.chosenAnswer());

        boolean isCorrect = checkAnswer(examProblemDto.problem().id(), examProblemDto.chosenAnswer());
        examProblem.setIsCorrect(isCorrect);

        examProblemRepository.save(examProblem);

        if (!isCorrect) {
            IncorrectAnswer incorrectAnswer = IncorrectAnswer.of(member, problem, examInformation, examProblemDto.chosenAnswer());
            incorrectAnswerRepository.save(incorrectAnswer);
        }

        return ExamProblemResultDto.from(examProblem);
    }

    /**
     * 모의고사 완료 및 점수 저장하기
     */
    public ExamInformationDto completeMockExam(ExamInformationDto examInformationDto) {
        ExamInformation examInformation = examInformationRepository.findById(examInformationDto.id())
            .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));
        examInformation.setSolvedDate(LocalDateTime.now());

        List<ExamProblem> answers = examProblemRepository.findByExamInformationId(examInformationDto.id());
        int score = (int) answers.stream().filter(ExamProblem::getIsCorrect).count() * 5;
        examInformation.setScore(score);
        examInformation.setRequiredTime(Duration.between(examInformation.getCreatedDate(), examInformation.getSolvedDate()));

        examInformationRepository.save(examInformation);
        return ExamInformationDto.from(examInformation);
    }

    /**
     * 틀린 문제 리스트 조회하기
     */
    public List<IncorrectAnswerDto> getIncorrectAnswers(Long memberId) {
        List<IncorrectAnswer> incorrectAnswers = incorrectAnswerRepository.findByMemberId(memberId);
        return incorrectAnswers.stream().map(IncorrectAnswerDto::from).collect(Collectors.toList());
    }

    /**
     * 틀린 문제 상세 조회하기
     */
    public IncorrectAnswerDto getIncorrectAnswer(Long id) {
        IncorrectAnswer incorrectAnswer = incorrectAnswerRepository.findById(id)
            .orElseThrow(() -> new IncorrectAnswerException(IncorrectAnswerErrorCode.NOT_FOUND_INCORRECT_ANSWER));
        return IncorrectAnswerDto.from(incorrectAnswer);
    }
}
