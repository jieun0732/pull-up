package pull_up.domain.deprecated;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.member.dto.MemberScoreDto;
import pull_up.infra.database.entity.legacy.MemberL;
import pull_up.domain.member.exception.MemberErrorCode;
import pull_up.domain.member.exception.MemberException;
import pull_up.infra.database.repository.member.MemberRepositoryL;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceL {

    private final MemberRepositoryL memberRepositoryL;

    /**
     * 특정 ID를 가진 Member의 정보를 조회하고, 가장 최근 ExamInformation의 Score와 함께 반환하는 메서드.
     */
    public MemberScoreDto getMemberById(Long id) {
        MemberL memberL = memberRepositoryL.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER)); // 예외 던지기


        // MemberDto를 생성하여 반환
        return MemberScoreDto.of(
                memberL.getId(),
                memberL.getName(),
                memberL.getEmail(),
                memberL.isAccessCheck(),
                memberL.getRole(),
                memberL.getExamLList()// 최신 Score 함께 반환
        );
    }


    /**
     * 특정 ID를 가진 Member의 accessCheck을 true로 변경하는 메서드.
     */
    public MemberDto updateAccessCheck(Long id) {
        Optional<MemberL> memberOptional = memberRepositoryL.findById(id);
        if (memberOptional.isPresent()) {
            MemberL memberL = memberOptional.get();
            memberL.setAccessCheck(true);
            memberRepositoryL.save(memberL);
            return new MemberDto(memberL.getId(), memberL.getName(), memberL.getEmail(), memberL.isAccessCheck(),
                    memberL.getRole());
        }
        return null; // 또는 예외를 던지도록 처리할 수 있음
    }

    /**
     * 멤버 탈퇴.
     */
    public void deleteMember(Long id) {
        MemberL memberL = memberRepositoryL.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER)); // 예외 던지기
        memberL.softDelete();
    }

    public void deleteMemberHard(Long id) {
        MemberL memberL = memberRepositoryL.findMemberByIdWithRelation(id);
        memberRepositoryL.delete(memberL);
    }
}
