package pull_up.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.exam.dto.Solved;
import pull_up.domain.member.dto.MemberInfo;
import pull_up.domain.member.exception.MemberException;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.entity.Member;

import java.util.Optional;

import static pull_up.domain.member.exception.MemberErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ExamRepository examRepository;

    public MemberInfo.Response getMemberInfo(Long memberId) {
        Member memberInDB = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));

        Optional<Exam> exam = examRepository.findMockExamByMemberId(memberId);

        return exam.map(notNullExam -> MemberInfo.Response.toDto(memberInDB, notNullExam))
                .orElseGet(() -> MemberInfo.Response.toDto(memberInDB));
    }
}
