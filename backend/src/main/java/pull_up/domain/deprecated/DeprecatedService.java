package pull_up.domain.deprecated;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.member.dto.MemberAnswerResponseDto;
import pull_up.api.member.dto.MemberAnswerResultDto;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.global.entity.BaseEntity;
import pull_up.domain.answer.exception.AnswerErrorCode;
import pull_up.domain.answer.exception.AnswerException;
import pull_up.infra.database.entity.legacy.*;
import pull_up.infra.database.entity.legacy.MemberL;
import pull_up.infra.database.repository.exam.ExamRepositoryL;
import pull_up.infra.database.repository.legacy.IncorrectAnswerRepository;
import pull_up.infra.database.repository.legacy.MemberAnswerRepository;
import pull_up.infra.database.repository.problem.ProblemRepositoryL;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeprecatedService {

    private final MemberAnswerRepository memberAnswerRepository;
    private final IncorrectAnswerRepository incorrectAnswerRepository;
    private final ProblemRepositoryL problemRepositoryL;
    private final ExamRepositoryL examRepositoryL;

    @Deprecated
    @Transactional
    public MemberAnswerResultDto saveMemberAnswer(MemberDto memberDTO, ProblemDto problemDTO, ExamInformationDto examInformationDTO, String chosenAnswer) {
        MemberL memberL = MemberDto.toEntity(memberDTO);
        ProblemL problemL = problemRepositoryL.findById(problemDTO.id()).orElseThrow();
        ExamL examL = examRepositoryL.findById(examInformationDTO.id()).orElseThrow();
        boolean isCorrect = chosenAnswer.equals(problemL.getAnswer());

        problemL.setTotalAttempts(problemL.getTotalAttempts() + 1);
        if (!isCorrect) {
            problemL.setIncorrectAttempts(problemL.getIncorrectAttempts() + 1);
        }
        problemL.setIncorrectRate((double) problemL.getIncorrectAttempts() / problemL.getTotalAttempts() * 100);
        problemRepositoryL.save(problemL);

        MemberAnswer memberAnswer = MemberAnswer.of(memberL, problemL, examL, chosenAnswer, isCorrect);
        memberAnswerRepository.save(memberAnswer);

        return MemberAnswerResultDto.from(memberAnswer);
    }

    @Deprecated
    public List<MemberAnswerResultDto> getIncorrectAnswers(MemberDto memberDTO, String category, String entry, String type) {
        MemberL memberL = MemberDto.toEntity(memberDTO);
        List<MemberAnswer> incorrectAnswers = memberAnswerRepository.findIncorrectAnswers(memberL, category, entry, type);
        return incorrectAnswers.stream().map(MemberAnswerResultDto::from).collect(Collectors.toList());
    }

    /**
     * 문제 답안 저장하기.
     */
    @Deprecated
    public MemberAnswerResultDto saveAnswer(MemberAnswerResponseDto memberAnswerResponseDto) {
        MemberAnswer memberAnswer = memberAnswerRepository.findById(memberAnswerResponseDto.id())
                .orElseThrow(
                        () -> new AnswerException(AnswerErrorCode.NOT_FOUND));

        // 3. 사용자가 제출한 답안을 설정
        memberAnswer.setChosenAnswer(memberAnswerResponseDto.chosenAnswer());

        ProblemL problemL = memberAnswer.getProblemL();
        MemberL memberL = memberAnswer.getMemberL();

        // 4. 정답 여부를 판별
        boolean isCorrect = checkAnswer(problemL.getId(),
                memberAnswerResponseDto.chosenAnswer());
        memberAnswer.setIsCorrect(isCorrect);

        // 5. MemberAnswer를 저장
        memberAnswerRepository.save(memberAnswer);

        // 6. IncorrectAnswer 처리
        Optional<IncorrectAnswer> existingIncorrectAnswer = incorrectAnswerRepository.findByMemberLAndProblemL(
                memberL, problemL);

        if (isCorrect) {
            // 정답일 경우 기존 오답 기록이 있으면 삭제
            existingIncorrectAnswer.ifPresent(BaseEntity::softDelete);
        } else {
            if (existingIncorrectAnswer.isPresent()) {
                // 오답일 경우 기존 오답 기록이 있으면 LocalDateTime 업데이트
                pull_up.infra.database.entity.legacy.IncorrectAnswer incorrectAnswer = existingIncorrectAnswer.get();
                incorrectAnswer.setIncorrectTime(LocalDateTime.now());
                incorrectAnswer.setChosenAnswer(memberAnswerResponseDto.chosenAnswer());
                incorrectAnswerRepository.save(incorrectAnswer);
            } else {
                // 오답일 경우 기존 오답 기록이 없으면 새로 저장
                pull_up.infra.database.entity.legacy.IncorrectAnswer incorrectAnswer = pull_up.infra.database.entity.legacy.IncorrectAnswer.of(memberL, problemL, null,
                        memberAnswerResponseDto.chosenAnswer(), LocalDateTime.now());
                incorrectAnswerRepository.save(incorrectAnswer);
            }
        }

        problemL.setTotalAttempts(problemL.getTotalAttempts() + 1);
        if (!isCorrect) {
            problemL.setIncorrectAttempts(problemL.getIncorrectAttempts() + 1);
        }
        problemL.setIncorrectRate(
                (double) problemL.getIncorrectAttempts() / problemL.getTotalAttempts() * 100);
        problemRepositoryL.save(problemL);

        // 7. 결과를 DTO로 변환하여 반환
        return MemberAnswerResultDto.from(memberAnswer);
    }

    /**
     * 문제의 답과 사용의 답 확인하기.
     */
    private boolean checkAnswer(Long problemId, String chosenAnswer) {
        ProblemL problemL = problemRepositoryL.findById(problemId).orElseThrow();
        return problemL.getAnswer().equals(chosenAnswer);
    }

    /**
     * 문제 리스트 조회 (골고루 풀기 및 유형별 풀기).
     */
    @Deprecated
    public List<MemberAnswerResultDto> getProblemList(Long memberId, String entry, String category,
                                                      String type) {
        List<MemberAnswer> memberAnswers = memberAnswerRepository.findByMemberAndOptionalFilters(
                memberId, entry, category, type);
        return memberAnswers.stream().map(MemberAnswerResultDto::from).collect(Collectors.toList());
    }

}
