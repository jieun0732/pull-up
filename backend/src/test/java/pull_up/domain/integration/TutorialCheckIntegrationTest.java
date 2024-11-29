package pull_up.domain.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.api.dto.MessageDto;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.member.MemberService;
import pull_up.domain.member.dto.SolvedInfo;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class TutorialCheckIntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ExamRepository examRepository;

    MemberService memberService;

    @BeforeEach
    void init() {
        memberService = new MemberService(memberRepository, examRepository, null);
    }

    @Test
    @DisplayName("튜토리얼 확인 통합 테스트")
    void testCheckTutorial() {
        Member member = MemberFixture.APPLE_USER.get();

        /* 1. 튜토리얼 여부 확인 : false */
        SolvedInfo.MockExamResponse solvedInfo = memberService.getSolvedInfo(member.getId());
        assertThat(solvedInfo.tutorialFinished()).isFalse();

        /* 2. 튜토리얼 확인 */
        MessageDto message = memberService.tutorialCheck(member.getId());
        assertThat(message.message()).isEqualTo("튜토리얼을 완료했습니다.");

        /* 3. 튜토리얼 여부 확인 : true */
        solvedInfo = memberService.getSolvedInfo(member.getId());
        assertThat(solvedInfo.tutorialFinished()).isTrue();
    }
}
