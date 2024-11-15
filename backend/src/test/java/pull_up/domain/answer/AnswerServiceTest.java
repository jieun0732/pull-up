package pull_up.domain.answer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pull_up.api.answer.dto.AnswerDto;
import pull_up.api.answer.dto.AnswerSubmit;
import pull_up.global.exception.member.AnswerErrorCode;
import pull_up.global.exception.member.AnswerException;
import pull_up.infra.database.entity.Answer;
import pull_up.infra.database.fixture.AnswerFixture;
import pull_up.infra.database.fixture.ExamFixture;
import pull_up.infra.database.fixture.MemberFixture;
import pull_up.infra.database.repository.answer.AnswerRepository;
import pull_up.infra.database.repository.exam.ExamRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

class AnswerServiceTest {

    AnswerService suit;

    ExamRepository mockExamRepository;

    AnswerRepository mockAnswerRepository;

    @BeforeEach
    void init() {
        mockExamRepository = Mockito.mock(ExamRepository.class);
        mockAnswerRepository = Mockito.mock(AnswerRepository.class);
        suit = new AnswerService(mockExamRepository, mockAnswerRepository);
    }

    @Test
    @DisplayName("Answer 생성 테스트")
    void testSubmit() {
        // given
        AnswerSubmit.Request correstRequest = new AnswerSubmit.Request(1L, 1L, "3");
        AnswerSubmit.Request incorrestRequest = new AnswerSubmit.Request(1L,2L,  "3");

        // when
        when(mockExamRepository.findByIdWithAnswer(correstRequest.examId())).thenReturn(Optional.of(ExamFixture.MATHEMATICS.get(MemberFixture.APPLE_USER.get())));
        AnswerSubmit.Response correctResponse = suit.submit(correstRequest);
        AnswerSubmit.Response incorrectResponse = suit.submit(incorrestRequest);

        // then
        assertThat(correctResponse.answer().chosenAnswer()).isEqualTo(3);
        assertThat(correctResponse.answer().correctAnswer()).isEqualTo(3);
        assertThat(correctResponse.answer().isCorrect()).isTrue();
        assertThat(incorrectResponse.answer().chosenAnswer()).isEqualTo(3);
        assertThat(incorrectResponse.answer().correctAnswer()).isEqualTo(2);
        assertThat(incorrectResponse.answer().isCorrect()).isFalse();
    }

    @Test
    @DisplayName("푼 문제 조회 테스트")
    void testGetSolved() {
        // given
        Long correctId = 1L;
        Long incorrectId = 2L;
        Long notSolvedId = 3L;
        Answer correctAnswer = AnswerFixture.NO_CHOSEN_1.get();
        Answer incorrectAnswer = AnswerFixture.NO_CHOSEN_2.get();
        correctAnswer.setChosenAnswer("3");
        correctAnswer.setIsCorrect(true);
        correctAnswer.setIsSolved(true);
        incorrectAnswer.setChosenAnswer("3");
        incorrectAnswer.setIsCorrect(false);
        incorrectAnswer.setIsSolved(true);

        // when
        when(mockAnswerRepository.findByIdWithProblem(correctId, true)).thenReturn(Optional.of(correctAnswer));
        when(mockAnswerRepository.findByIdWithProblem(incorrectId, true)).thenReturn(Optional.of(incorrectAnswer));
        when(mockAnswerRepository.findByIdWithProblem(notSolvedId, true)).thenReturn(Optional.empty());

        AnswerDto correctResponse = suit.getSolved(correctId);
        AnswerDto incorrectResponse = suit.getSolved(incorrectId);

        // then
        assertThat(correctResponse.chosenAnswer()).isEqualTo(3);
        assertThat(correctResponse.correctAnswer()).isEqualTo(3);
        assertThat(correctResponse.isCorrect()).isTrue();
        assertThat(incorrectResponse.chosenAnswer()).isEqualTo(3);
        assertThat(incorrectResponse.correctAnswer()).isEqualTo(2);
        assertThat(incorrectResponse.isCorrect()).isFalse();
        assertThatThrownBy(() -> suit.getSolved(notSolvedId)).isInstanceOf(AnswerException.class)
                .hasMessage(AnswerErrorCode.NOT_FOUND.getMessage());
    }
}