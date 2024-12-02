package pull_up.domain.member.dto;

import pull_up.domain.auth.SNSProvider;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.entity.Member;

public record MemberInfo() {
    public record Response(
            Long memberId,
            String name,
            String email,
            SNSProvider snsProvider,
            Boolean mockExamSolved,
            Integer mockExamScore
    ) {
        public static Response toDto(Member member, Exam exam) {
            return new Response(
                    member.getId(),
                    member.getName(),
                    member.getPrivateEmail(),
                    member.getSnsProvider(),
                    exam.getIsFinished(),
                    exam.getScore()
            );
        }

        public static Response toDto(Member member) {
            return new Response(
                    member.getId(),
                    member.getName(),
                    member.getPrivateEmail(),
                    member.getSnsProvider(),
                    false,
                    0
            );
        }
    }
}
