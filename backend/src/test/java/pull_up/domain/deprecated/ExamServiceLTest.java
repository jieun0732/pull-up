package pull_up.domain.deprecated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.api.exam.dto.ExamGrade;
import pull_up.api.exam.dto.IncorrectAnswer;
import pull_up.global.dto.ListDto;
import pull_up.infra.database.jpa.entity.legacy.AnswerL;
import pull_up.infra.database.jpa.entity.legacy.ExamL;
import pull_up.infra.database.jpa.entity.legacy.MemberL;
import pull_up.infra.database.jpa.fixture.legacy.ExamFixture;
import pull_up.infra.database.jpa.fixture.legacy.MemberFixture;
import pull_up.infra.database.jpa.repository.answer.AnswerRepositoryL;
import pull_up.infra.database.jpa.repository.legacy.ExamLRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.mock;

class ExamServiceLTest {

    ExamLRepository examRepositoryL;
    AnswerRepositoryL answerRepositoryL;

    ExamServiceL suit;

    MemberL memberL;
    ExamL examL;

    @BeforeEach
    void init() {
        examRepositoryL = mock(ExamLRepository.class);
        answerRepositoryL = mock(AnswerRepositoryL.class);
        suit = new ExamServiceL(null,
                null,
                null,
                null,
                examRepositoryL,
                answerRepositoryL);
        memberL = MemberFixture.APPLE_USER.get();
        examL = ExamFixture.MATHEMATICS.get(memberL);
    }

    @Test
    @DisplayName("모의고사 완료 후 문제 제출 시 채점하여 결과 전송, 틀린문제 수정되는지 확인")
    void testGrade() {
        // given
        Long examId = 1L;
        ExamGrade.Request request = new ExamGrade.Request(examId, List.of(
                new ExamGrade.SelectedAnswer(1L, 3),
                new ExamGrade.SelectedAnswer(2L, 3),
                new ExamGrade.SelectedAnswer(3L, 3),
                new ExamGrade.SelectedAnswer(4L, 3),
                new ExamGrade.SelectedAnswer(5L, 3)
        ));

        Long memberId = 1L;
        IncorrectAnswer.Brief expect1 = new IncorrectAnswer.Brief(2L, 2L, "수리", "골고루", "속력", "민수는 원형 트랙을 자전거로 시계 방향으로 돌면서 첫 30분 동안 4바퀴, 그 다음 30분 동안 5바퀴, 마지막 1시간 동안 10바퀴를 돌았다. 원형 트랙의 총 길이가 6km라면, 민수가 2시간 동안 자전거를 탄 평균 속력은 몇 km/h인가?", LocalDateTime.of(2024, 11, 13, 22, 3));
        IncorrectAnswer.Brief expect2 = new IncorrectAnswer.Brief(3L, 3L, "수리", "골고루", "용액의 농도", "어떤 소금물 500g에 100g 물을 증발시켰더니 15%의 소금물이 되었다. 처음 소금물의 농도는 몇 %인가?", LocalDateTime.of(2024, 11, 13, 22, 3));
        IncorrectAnswer.Brief expect3 = new IncorrectAnswer.Brief(4L, 4L, "수리", "골고루", "용액의 농도", "어떤 소금물 600g에 300g 물을 증발시켰더니 20%의 소금물이 나왔다. 처음 소금물의 농도는 몇 %인가?", LocalDateTime.of(2024, 11, 13, 22, 3));
        List<AnswerL> incorrectAnswerLS = List.of(
                examL.getAnswerLS().get(1),
                examL.getAnswerLS().get(2),
                examL.getAnswerLS().get(3));

        Long answerId = 1L;
        AnswerL incorrectAnswerL = examL.getAnswerLS().get(1);
        IncorrectAnswer.Detail expectDetail = new IncorrectAnswer.Detail(2L, 2L, "수리", "골고루", "속력", "민수는 원형 트랙을 자전거로 시계 방향으로 돌면서 첫 30분 동안 4바퀴, 그 다음 30분 동안 5바퀴, 마지막 1시간 동안 10바퀴를 돌았다. 원형 트랙의 총 길이가 6km라면, 민수가 2시간 동안 자전거를 탄 평균 속력은 몇 km/h인가?", "", List.of("48 km/h", "57 km/h", "60 km/h", "66 km/h", "78 km/h"), "3", "2", "민수가 자전거로 총 달린 거리를 구합니다.\n" +
                " 첫 30분 동안: 6km X 4바퀴 = 24km\n" +
                " 다음 30분 동안: 6km X 5바퀴 = 30km\n" +
                " 마지막 1시간 동안: 6km X 10바퀴 = 60km\n" +
                " 따라서 총 이동 거리는 24 + 30 + 60 = 114km입니다.\n" +
                " 평균 속력은 총 이동 거리(114km)를 총 시간(2시간)으로 나눈 값입니다.\n" +
                " 즉, 114 / 2 = 57 km/h 입니다.", 100D);

        // when : 채점 및 틀린문제 확인
        when(examRepositoryL.findByIdWithAnswer(examId)).thenReturn(Optional.of(examL));
        when(answerRepositoryL.findIncorrectAnswersByMemberId(memberId)).thenReturn(incorrectAnswerLS);
        when(answerRepositoryL.findByIdWithProblem(answerId)).thenReturn(Optional.of(incorrectAnswerL));

        ExamGrade.Response response = suit.grade(request);
        ListDto<IncorrectAnswer.Brief> incorrectAnswersResponse = suit.getIncorrectAnswers(memberId);
        IncorrectAnswer.Detail incorrectAnswerDetail = suit.getIncorrectAnswerDetail(answerId);

        // then 1 : 정답 2개, 오답 3개, 정답률 40%
        assertThat(response.totalCount()).isSameAs(5);
        assertThat(response.correctCount()).isSameAs(2);
        assertThat(response.incorrectCount()).isSameAs(3);
        assertThat(response.correctRate()).isEqualTo((double) 2 / 5);
        assertThat(examL.getAnswerLS().get(0).getProblemL().getIncorrectRate()).isEqualTo(0);
        assertThat(examL.getAnswerLS().get(1).getProblemL().getIncorrectRate()).isEqualTo(100);
        assertThat(examL.getAnswerLS().get(2).getProblemL().getIncorrectRate()).isEqualTo(100);
        assertThat(examL.getAnswerLS().get(3).getProblemL().getIncorrectRate()).isEqualTo(100);
        assertThat(examL.getAnswerLS().get(4).getProblemL().getIncorrectRate()).isEqualTo(0);
        assertThat(examL.getSolvedTime()).isCloseTo(LocalDateTime.now(Clock.systemDefaultZone()), within(1, ChronoUnit.SECONDS));

        // then 2 : 틀린문제 3개 확인
        assertThat(incorrectAnswersResponse.list()).hasSize(3)
                .anySatisfy((res) -> assertThat(res).usingRecursiveComparison().ignoringFields("answerId", "solvedTime").isEqualTo(expect1))
                .anySatisfy((res) -> assertThat(res).usingRecursiveComparison().ignoringFields("answerId", "solvedTime").isEqualTo(expect2))
                .anySatisfy((res) -> assertThat(res).usingRecursiveComparison().ignoringFields("answerId", "solvedTime").isEqualTo(expect3));

        // then 3: 틀린문제 세부정보 확인
        assertThat(incorrectAnswerDetail).usingRecursiveComparison().ignoringFields("answerId").isEqualTo(expectDetail);
    }
}