package pull_up.domain.answer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pull_up.api.answer.dto.AnswerDto;
import pull_up.api.answer.dto.AnswerSolved;
import pull_up.api.answer.dto.AnswerSubmit;
import pull_up.domain.deprecated.AnswerService;
import pull_up.global.exception.member.AnswerErrorCode;
import pull_up.global.exception.member.AnswerException;
import pull_up.infra.database.entity.legacy.AnswerL;
import pull_up.infra.database.fixture.legacy.ExamFixture;
import pull_up.infra.database.fixture.legacy.MemberFixture;
import pull_up.infra.database.repository.answer.AnswerRepository;
import pull_up.infra.database.repository.exam.ExamRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pull_up.infra.database.fixture.legacy.AnswerFixture.*;
import static pull_up.infra.database.fixture.legacy.AnswerFixture.SOLVED_5;

class AnswerLServiceTest {

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
        AnswerL correctAnswerL = NO_CHOSEN_1.get();
        AnswerL incorrectAnswerL = NO_CHOSEN_2.get();
        correctAnswerL.setChosenAnswer("3");
        correctAnswerL.setIsCorrect(true);
        correctAnswerL.setIsSolved(true);
        incorrectAnswerL.setChosenAnswer("3");
        incorrectAnswerL.setIsCorrect(false);
        incorrectAnswerL.setIsSolved(true);

        // when
        when(mockAnswerRepository.findByIdWithProblem(correctId, true)).thenReturn(Optional.of(correctAnswerL));
        when(mockAnswerRepository.findByIdWithProblem(incorrectId, true)).thenReturn(Optional.of(incorrectAnswerL));
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

    @Test
    @DisplayName("푼 문제 전체조회 테스트")
    void testGetSolvedAll() {
        // given
        Long memberId = 1L;
        String entry = "수리";
        List<AnswerL> answerLS = List.of(
                SOLVED_1.get(),
                SOLVED_2.get(),
                SOLVED_3.get(),
                SOLVED_4.get(),
                SOLVED_5.get());

        // when
        when(mockAnswerRepository.findSolvedAnswersByMemberIdAndEntry(memberId, entry)).thenReturn(answerLS);
        AnswerSolved response = suit.getSolvedAll(memberId, entry);

        // then
//        assertThat(response.entry()).isEqualTo(entry);
//        assertThat(response.answerTypeCount()).isEqualTo(2);
//        assertThat(response.answerTypes()).hasSize(response.answerTypeCount());
//        assertThat(response.isSolvedEvenly()).isFalse();
    }
}