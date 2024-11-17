package pull_up.api.member.dto;

import pull_up.domain.auth.dto.OAuth2LoginResponseDto;
import pull_up.infra.database.entity.legacy.ExamL;

import java.util.Comparator;
import java.util.List;

public record MemberScoreDto (
    Long id,
    String name,
    String email,
    boolean accessCheck,
    String role,
    Integer latestScore // 최신 Score 필드 추가
) {
    public static MemberScoreDto of(Long id, String name, String email, boolean accessCheck, String role, List<ExamL> scores) {

        // 가장 최근의 ExamInformation의 Score를 가져오기
        Integer latestScore = scores == null? Integer.valueOf(0) : scores.stream()
                .max(Comparator.comparing(ExamL::getCreatedDate)) // 가장 최근의 ExamInformation 찾기
                .map(ExamL::getScore) // Score 반환
                .orElse(null); // Score가 없으면 null 반환

        return new MemberScoreDto(id, name, getPrivateEmail(email), accessCheck, role, latestScore);
    }

    public static String getPrivateEmail(String email) {
        return OAuth2LoginResponseDto.getPrivateEmail(email);
    }
}
