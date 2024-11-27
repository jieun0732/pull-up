package pull_up.infra.database.jpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.fixture.FixtureRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {

    @Test
    @DisplayName("채점 테스트")
    void testMark() {
        // given
        Integer submitAnswer = 3;
        Answer emptyAnswer = FixtureRepository.getEmptyEvenlyAnswer(1, 1L, Entry.MATH);

        // when
        boolean correct = emptyAnswer.mark(submitAnswer);

        // then
        assertThat(correct).isTrue();
        assertThat(emptyAnswer.getSubmitAnswerToInt()).isEqualTo(submitAnswer);
        assertThat(emptyAnswer.getIsCorrect()).isTrue();
        assertThat(emptyAnswer.getSubmitCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("푼 날짜 생성 테스트")
    void testSolvedDate() {
        // given
        Exam exam = FixtureRepository.getEvenlyExam(1L, Entry.MATH);
        exam.submit(1, 3);
        Answer answer = exam.getAnswers().get(0);

        // when
        String solvedDate = answer.getSolvedDate();

        // then
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일");
        assertThat(solvedDate).isEqualTo(LocalDateTime.now().format(formatter));
    }
}