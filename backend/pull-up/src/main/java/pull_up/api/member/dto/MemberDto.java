package pull_up.api.member.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import pull_up.api.member.entity.Member;

/**
 * DTO for {@link Member}
 */
public record MemberDto(Long id, String name, String email, boolean accessCheck) implements
    Serializable {

    public static MemberDto of(Long id, String name, String email, boolean accessCheck, LocalDateTime deletedAt) {
        return new MemberDto(id, name, email, accessCheck);
    }

    /**
     * Member 엔티티를 MemberDto로 변환하는 메소드.
     */
    public static MemberDto from(Member entity) {
        return new MemberDto(entity.getId(), entity.getName(), entity.getEmail(), entity.isAccessCheck());
    }

    /**
     * MemberDto를 Member로 변환하는 메소드.
     */
    public static Member toEntity(MemberDto dto) {
        return Member.of(dto.name(), dto.email(), dto.accessCheck());
    }
}