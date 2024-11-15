package pull_up.domain.answer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import pull_up.api.answer.dto.CreateAnswer;
import pull_up.infra.database.fixture.ExamFixture;
import pull_up.infra.database.fixture.MemberFixture;
import pull_up.infra.database.repository.exam.ExamRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerServiceTest {

    AnswerService suit;

    ExamRepository mockRepository;

    @BeforeEach
    void init() {
        mockRepository = Mockito.mock(ExamRepository.class);
        suit = new AnswerService(mockRepository);
    }

    @Test
    @DisplayName("Answer 생성 테스트")
    void testSubmit() {
        // given
        CreateAnswer.Request correstRequest = new CreateAnswer.Request(1L, 1L, "3");
        CreateAnswer.Request incorrestRequest = new CreateAnswer.Request(1L,2L,  "3");

        // when
        BDDMockito.when(mockRepository.findByIdWithAnswer(correstRequest.examId())).thenReturn(Optional.of(ExamFixture.MATHEMATICS.get(MemberFixture.APPLE_USER.get())));
        CreateAnswer.Response correctResponse = suit.submit(correstRequest);
        CreateAnswer.Response incorrectResponse = suit.submit(incorrestRequest);

        // then
        assertThat(correctResponse.chosenAnswer()).isEqualTo("3");
        assertThat(correctResponse.correctAnswer()).isEqualTo("3");
        assertThat(correctResponse.isCorrect()).isTrue();
        assertThat(incorrectResponse.chosenAnswer()).isEqualTo("3");
        assertThat(incorrectResponse.correctAnswer()).isEqualTo("2");
        assertThat(incorrectResponse.isCorrect()).isFalse();
    }
}