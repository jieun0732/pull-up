package pull_up.api.member.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.member.entity.Member;
import pull_up.api.member.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 특정 ID를 가진 Member를 조회하는 메서드.
     */
    public MemberDto getMemberById(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return new MemberDto(member.getId(), member.getName(), member.getEmail(), member.isAccessCheck());
        }
        return null; // 또는 예외를 던지도록 처리할 수 있음
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
            return new MemberDto(member.getId(), member.getName(), member.getEmail(), member.isAccessCheck());
        }
        return null; // 또는 예외를 던지도록 처리할 수 있음
    }

}
