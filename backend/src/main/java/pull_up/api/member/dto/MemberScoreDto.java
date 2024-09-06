package pull_up.api.member.dto;

public record MemberScoreDto (
    Long id,
    String name,
    String email,
    boolean accessCheck,
    String role,
    Integer latestScore // 최신 Score 필드 추가
) {
    public static MemberScoreDto of(Long id, String name, String email, boolean accessCheck, String role, Integer latestScore) {
        return new MemberScoreDto(id, name, email, accessCheck, role, latestScore);
    }
}
