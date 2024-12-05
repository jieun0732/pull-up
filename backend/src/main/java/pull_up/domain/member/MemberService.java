package pull_up.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pull_up.api.dto.ListDto;
import pull_up.api.dto.MessageDto;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.exam.TempExam;
import pull_up.domain.member.dto.SolvedInfo;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.dto.IncorrectQueryDto;
import pull_up.domain.member.dto.MemberInfo;
import pull_up.domain.member.exception.MemberException;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.entity.Member;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pull_up.domain.member.exception.MemberErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ExamRepository examRepository;
    private final ProblemRepository problemRepository;

    public SolvedInfo.ByEntryResponse getSolvedInfo(Long memberId, Entry entry) {
        Map<String, Exam> examMap = examRepository.findAllEvenlyAndProblemTypeExamMap(memberId, entry);
        Map<String, Integer> problemTypesMap = problemRepository.findAllProblemTypeAndCountExceptProblemsheetByEntry(entry);

        for (Map.Entry<String, Integer> problemTypeEntry : problemTypesMap.entrySet()) {
            if (examMap.containsKey(problemTypeEntry.getKey())) continue;
            examMap.put(problemTypeEntry.getKey(), new TempExam(problemTypeEntry.getValue()));
        }

        return SolvedInfo.ByEntryResponse.toDto(entry, examMap);
    }

    public SolvedInfo.MockExamResponse getSolvedInfo(Long memberId) {
        boolean tutorialFinished = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER)).getTutorialFinished();
        Optional<Exam> exam = examRepository.findMockExamByMemberId(memberId);

        return exam.map(e -> SolvedInfo.MockExamResponse.toDto(tutorialFinished, e))
                .orElseGet(() -> SolvedInfo.MockExamResponse.empty(tutorialFinished));
    }

    public MemberInfo.Response getMemberInfo(Long memberId) {
        Member memberInDB = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
        Optional<Exam> exam = examRepository.findMockExamByMemberId(memberId);

        return exam.map(notNullExam -> MemberInfo.Response.toDto(memberInDB, notNullExam))
                .orElseGet(() -> MemberInfo.Response.toDto(memberInDB));
    }

    public ListDto<IncorrectQueryDto> getIncorrect(Long memberId) {
        List<IncorrectQueryDto> incorrectAnswers = memberRepository.getIncorrectAnswersById(memberId);
        return new ListDto<>(incorrectAnswers);
    }

    @Transactional
    public MessageDto tutorialCheck(Long memberId) {
        Member memberInDB = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
        memberInDB.finishTutorial();
        return new MessageDto("튜토리얼을 완료했습니다.");
    }

    @Transactional
    public MessageDto delete(Long memberId) {
        examRepository.findAllByMemberId(memberId).forEach(Exam::deleteMember);
        memberRepository.deleteById(memberId);
        return new MessageDto("회원탈퇴가 완료되었습니다.");
    }
}
