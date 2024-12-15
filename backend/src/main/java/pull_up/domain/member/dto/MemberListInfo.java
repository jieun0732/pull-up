package pull_up.domain.member.dto;

import pull_up.global.util.GlobalFormatter;
import pull_up.infra.database.jpa.entity.Member;

public record MemberListInfo(
        Long id,
        Boolean tutorialFinished,
        String createdTime,
        String email,
        String name,
        String snsProvider
) {

    public static MemberListInfo toDto(Member member) {
        return new MemberListInfo(
                member.getId(),
                member.getTutorialFinished(),
                member.getCreatedTime().format(GlobalFormatter.KOREAN_DATE_FORMATTER),
                member.getPrivateEmail(),
                member.getName(),
                member.getSnsProvider().name());
    }
}
