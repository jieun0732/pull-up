package pull_up.api.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.exam.repository.ExamInformationRepository;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.member.dto.MemberScoreDto;
import pull_up.api.member.entity.Member;
import pull_up.api.member.exception.MemberErrorCode;
import pull_up.api.member.exception.MemberException;
import pull_up.api.member.repository.MemberRepository;

import java.util.Comparator;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 특정 ID를 가진 Member의 정보를 조회하고, 가장 최근 ExamInformation의 Score와 함께 반환하는 메서드.
     */
    public MemberScoreDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER)); // 예외 던지기


        // MemberDto를 생성하여 반환
        return MemberScoreDto.of(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.isAccessCheck(),
                member.getRole(),
                member.getExamInformations()// 최신 Score 함께 반환
        );
    }


    /**
     * 특정 ID를 가진 Member의 accessCheck을 true로 변경하는 메서드.
     */
    public MemberDto updateAccessCheck(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.setAccessCheck(true);
            memberRepository.save(member);
            return new MemberDto(member.getId(), member.getName(), member.getEmail(), member.isAccessCheck(),
                    member.getRole());
        }
        return null; // 또는 예외를 던지도록 처리할 수 있음
    }

    /**
     * 멤버 탈퇴.
     */
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER)); // 예외 던지기
        member.softDelete();
    }

    public void deleteMemberHard(Long id) {
        Member member = memberRepository.findMemberByIdWithRelation(id);
        memberRepository.delete(member);
    }
}
