package pull_up.domain.auth.dto;

import pull_up.domain.auth.SNSProvider;
import pull_up.domain.exam.dto.Grade;
import pull_up.infra.database.jpa.entity.Member;

public record OAuth2Login(

) {
    public record Request() {
        public record Local(
                String snsId,
                String name,
                String email
        ) {}

        public record Kakao(
                String code
        ) {}

        public record Apple(
                UserName name,
                String email
        ) {
            public record UserName(
                    String firstName,
                    String lastName
            ) {}
        }
    }

    public record Response(
            Boolean firstLogin,
            Long memberId,
            String provider,
            String name,
            String email
    ) {
        public static Response toDto(Member member, Boolean firstLogin) {
            return new Response(firstLogin, member.getId(), member.getSnsProvider().getValue(), member.getName(), member.getPrivateEmail());
        }
    }
}
