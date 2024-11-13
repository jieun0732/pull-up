package pull_up.domain.exam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.api.exam.dto.GradeExam;
import pull_up.infra.database.entity.AnsweredProblem;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.entity.Problem;
import pull_up.infra.database.fixture.ExamFixture;
import pull_up.infra.database.fixture.MemberFixture;
import pull_up.infra.database.fixture.ProblemFixture;
import pull_up.infra.database.repository.exam.ExamRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.mock;

class ExamServiceTest {

    ExamRepository examRepository;

    ExamService suit;

    Exam exam;

    @BeforeEach
    void init() {
        examRepository = mock(ExamRepository.class);
        suit = new ExamService(null,
                null,
                null,
                null,
                examRepository,
                null);
        exam = ExamFixture.MATHEMATICS.get(MemberFixture.APPLE_USER.get());
    }

    @Test
    @DisplayName("모의고사 완료 후 문제 제출 시 채점하여 결과 전송")
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

        // when
        when(examRepository.findByIdWithAnswer(examId)).thenReturn(Optional.of(exam));
        GradeExam.Response response = suit.grade(request);

        // then
        assertThat(response.totalCount()).isSameAs(5);
        assertThat(response.correctCount()).isSameAs(2);
        assertThat(response.wrongCount()).isSameAs(3);
        assertThat(response.correctRate()).isEqualTo((double) 2 / 5);

        // 정답률 변화 확인
        assertThat(exam.getAnsweredProblem().get(0).getProblem().getIncorrectRate()).isEqualTo(0);
        assertThat(exam.getAnsweredProblem().get(1).getProblem().getIncorrectRate()).isEqualTo(100);
        assertThat(exam.getAnsweredProblem().get(2).getProblem().getIncorrectRate()).isEqualTo(100);
        assertThat(exam.getAnsweredProblem().get(3).getProblem().getIncorrectRate()).isEqualTo(100);
        assertThat(exam.getAnsweredProblem().get(4).getProblem().getIncorrectRate()).isEqualTo(0);
    }

}