package pull_up.api.member.dto;

import pull_up.infra.database.entity.ExamInformation;

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
    public static MemberScoreDto of(Long id, String name, String email, boolean accessCheck, String role, List<ExamInformation> scores) {

        // 가장 최근의 ExamInformation의 Score를 가져오기
        Integer latestScore = scores == null? Integer.valueOf(0) : scores.stream()
                .max(Comparator.comparing(ExamInformation::getCreatedDate)) // 가장 최근의 ExamInformation 찾기
                .map(ExamInformation::getScore) // Score 반환
                .orElse(null); // Score가 없으면 null 반환

        return new MemberScoreDto(id, name, getPrivateEmail(email), accessCheck, role, latestScore);
    }

    public static String getPrivateEmail(String email) {
        if (email.equals("CONCEALED_EMAIL")) return email;
        if (email.split("@")[1].contains("private")) return "CONCEALED_EMAIL";
        return email;
    }
}
