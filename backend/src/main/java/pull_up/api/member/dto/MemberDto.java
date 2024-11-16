package pull_up.api.member.dto;

import java.io.Serializable;

import pull_up.infra.database.entity.legacy.MemberL;

/**
 * DTO for {@link MemberL}
 */
public record MemberDto(Long id, String name, String email, boolean accessCheck, String role) implements
    Serializable {

    public static MemberDto of(Long id, String name, String email, boolean accessCheck, String role) {
        return new MemberDto(id, name, email, accessCheck, role);
    }

    /**
     * Member 엔티티를 MemberDto로 변환하는 메소드.
     */
    public static MemberDto from(MemberL entity) {
        return new MemberDto(entity.getId(), entity.getName(), entity.getEmail(), entity.isAccessCheck(),
            entity.getRole());
    }

    /**
     * MemberDto를 Member로 변환하는 메소드.
     */
    public static MemberL toEntity(MemberDto dto) {
        return MemberL.of(dto.name(), dto.email(), dto.accessCheck(), dto.role());
    }
}