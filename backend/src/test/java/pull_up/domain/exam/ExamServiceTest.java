package pull_up.domain.exam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.api.exam.dto.GradeExam;
import pull_up.api.exam.dto.IncorrectAnswer;
import pull_up.global.dto.ListDto;
import pull_up.infra.database.entity.Answer;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.fixture.ExamFixture;
import pull_up.infra.database.fixture.MemberFixture;
import pull_up.infra.database.repository.answer.AnswerRepository;
import pull_up.infra.database.repository.exam.ExamRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.mock;

class ExamServiceTest {

    ExamRepository examRepository;
    AnswerRepository answerRepository;

    ExamService suit;

    Member member;
    Exam exam;

    @BeforeEach
    void init() {
        examRepository = mock(ExamRepository.class);
        answerRepository = mock(AnswerRepository.class);
        suit = new ExamService(null,
                null,
                null,
                null,
                examRepository,
                answerRepository);
        member = MemberFixture.APPLE_USER.get();
        exam = ExamFixture.MATHEMATICS.get(member);
    }

    @Test
    @DisplayName("모의고사 완료 후 문제 제출 시 채점하여 결과 전송, 틀린문제 수정되는지 확인")
    void testGrade() {
        // given
        Long examId = 1L;
        GradeExam.Request request = new GradeExam.Request(examId, List.of(
                new GradeExam.SelectedAnswer(1L, 3),
                new GradeExam.SelectedAnswer(2L, 3),
                new GradeExam.SelectedAnswer(3L, 3),
                new GradeExam.SelectedAnswer(4L, 3),
                new GradeExam.SelectedAnswer(5L, 3)
        ));

        Long memberId = 1L;
        IncorrectAnswer.Brief expect1 = new IncorrectAnswer.Brief(2L, 2L, "수리", "골고루", "속력", "민수는 원형 트랙을 자전거로 시계 방향으로 돌면서 첫 30분 동안 4바퀴, 그 다음 30분 동안 5바퀴, 마지막 1시간 동안 10바퀴를 돌았다. 원형 트랙의 총 길이가 6km라면, 민수가 2시간 동안 자전거를 탄 평균 속력은 몇 km/h인가?", LocalDateTime.of(2024, 11, 13, 22, 3));
        IncorrectAnswer.Brief expect2 = new IncorrectAnswer.Brief(3L, 3L, "수리", "골고루", "용액의 농도", "어떤 소금물 500g에 100g 물을 증발시켰더니 15%의 소금물이 되었다. 처음 소금물의 농도는 몇 %인가?", LocalDateTime.of(2024, 11, 13, 22, 3));
        IncorrectAnswer.Brief expect3 = new IncorrectAnswer.Brief(4L, 4L, "수리", "골고루", "용액의 농도", "어떤 소금물 600g에 300g 물을 증발시켰더니 20%의 소금물이 나왔다. 처음 소금물의 농도는 몇 %인가?", LocalDateTime.of(2024, 11, 13, 22, 3));
        List<Answer> incorrectAnswers = List.of(
                exam.getAnswers().get(1),
                exam.getAnswers().get(2),
                exam.getAnswers().get(3));

        Long answerId = 1L;
        Answer incorrectAnswer = exam.getAnswers().get(1);
        IncorrectAnswer.Detail expectDetail = new IncorrectAnswer.Detail(2L, 2L, "수리", "골고루", "속력", "민수는 원형 트랙을 자전거로 시계 방향으로 돌면서 첫 30분 동안 4바퀴, 그 다음 30분 동안 5바퀴, 마지막 1시간 동안 10바퀴를 돌았다. 원형 트랙의 총 길이가 6km라면, 민수가 2시간 동안 자전거를 탄 평균 속력은 몇 km/h인가?", "", List.of("48 km/h", "57 km/h", "60 km/h", "66 km/h", "78 km/h"), "3", "2", "민수가 자전거로 총 달린 거리를 구합니다.\n" +
                " 첫 30분 동안: 6km X 4바퀴 = 24km\n" +
                " 다음 30분 동안: 6km X 5바퀴 = 30km\n" +
                " 마지막 1시간 동안: 6km X 10바퀴 = 60km\n" +
                " 따라서 총 이동 거리는 24 + 30 + 60 = 114km입니다.\n" +
                " 평균 속력은 총 이동 거리(114km)를 총 시간(2시간)으로 나눈 값입니다.\n" +
                " 즉, 114 / 2 = 57 km/h 입니다.", 100D);

        // when : 채점 및 틀린문제 확인
        when(examRepository.findByIdWithAnswer(examId)).thenReturn(Optional.of(exam));
        when(answerRepository.findIncorrectAnswersByMemberId(memberId)).thenReturn(incorrectAnswers);
        when(answerRepository.findByIdWithProblem(answerId)).thenReturn(Optional.of(incorrectAnswer));

        GradeExam.Response response = suit.grade(request);
        ListDto<IncorrectAnswer.Brief> incorrectAnswersResponse = suit.getIncorrectAnswers(memberId);
        IncorrectAnswer.Detail incorrectAnswerDetail = suit.getIncorrectAnswerDetail(answerId);

        // then 1 : 정답 2개, 오답 3개, 정답률 40%
        assertThat(response.totalCount()).isSameAs(5);
        assertThat(response.correctCount()).isSameAs(2);
        assertThat(response.incorrectCount()).isSameAs(3);
        assertThat(response.correctRate()).isEqualTo((double) 2 / 5);
        assertThat(exam.getAnswers().get(0).getProblem().getIncorrectRate()).isEqualTo(0);
        assertThat(exam.getAnswers().get(1).getProblem().getIncorrectRate()).isEqualTo(100);
        assertThat(exam.getAnswers().get(2).getProblem().getIncorrectRate()).isEqualTo(100);
        assertThat(exam.getAnswers().get(3).getProblem().getIncorrectRate()).isEqualTo(100);
        assertThat(exam.getAnswers().get(4).getProblem().getIncorrectRate()).isEqualTo(0);
        assertThat(exam.getSolvedTime()).isCloseTo(LocalDateTime.now(Clock.systemDefaultZone()), within(1, ChronoUnit.SECONDS));

        // then 2 : 틀린문제 3개 확인
        assertThat(incorrectAnswersResponse.list()).hasSize(3)
                .anySatisfy((res) -> assertThat(res).usingRecursiveComparison().ignoringFields("answerId", "solvedTime").isEqualTo(expect1))
                .anySatisfy((res) -> assertThat(res).usingRecursiveComparison().ignoringFields("answerId", "solvedTime").isEqualTo(expect2))
                .anySatisfy((res) -> assertThat(res).usingRecursiveComparison().ignoringFields("answerId", "solvedTime").isEqualTo(expect3));

        // then 3: 틀린문제 세부정보 확인
        assertThat(incorrectAnswerDetail).usingRecursiveComparison().ignoringFields("answerId").isEqualTo(expectDetail);
    }
}