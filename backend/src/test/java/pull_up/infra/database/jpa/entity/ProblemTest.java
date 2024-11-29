package pull_up.infra.database.jpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.fixture.ProblemFixture;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemTest {

    @Test
    @DisplayName("공백 제거 테스트")
    void testNormalProblemType() {
        Problem problem = ProblemFixture.MATH_DENSITY_2.get();
        assertThat(problem.getNormalProblemType()).isEqualTo("용액의농도");
    }

    @Test
    @DisplayName("정답률 계산 테스트")
    void testCalculateCorrectRate() {
        Problem problem = ProblemFixture.MATH_DENSITY_2.get();

        // add correct answer
        problem.addTotalAttempt(true);
        assertThat(problem.getCorrectRate()).isEqualTo(100);
        assertThat(problem.getIncorrectRate()).isEqualTo(0);

        // add incorrect answer
        problem.addTotalAttempt(false);
        assertThat(problem.getCorrectRate()).isEqualTo(50);
        assertThat(problem.getIncorrectRate()).isEqualTo(50);
    }
    
    @Test
    @DisplayName("정답 변환")
    void testProblemToString() {
        // given
        Problem problem = ProblemFixture.MATH_DENSITY_2.get();
    
        // when
        Integer correctAnswer = problem.getCorrectAnswerToInt();

        // then
        assertThat(correctAnswer).isEqualTo(1);
    }

    @Test
    @DisplayName("문제 생성")
    void testProblemCreate() {
        // given
        Map<String, String> parameters = new HashMap<>();
        parameters.put("entry", "언어");
        parameters.put("problemType", "언어유형1");
        parameters.put("question", "질문");
        parameters.put("example", "보기");
        parameters.put("choice1", "선택1");
        parameters.put("choice2", "선택2");
        parameters.put("choice3", "선택3");
        parameters.put("choice4", "선택4");
        parameters.put("choice5", "선택5");
        parameters.put("explanation", "해설");

        // when
        Problem problem = Problem.create(parameters);

        // then
        assertThat(problem.getEntry()).isEqualTo(Entry.LANGUAGE);
        assertThat(problem.getProblemType()).isEqualTo("언어유형1");
        assertThat(problem.getQuestionAsString()).isEqualTo("질문");
        assertThat(problem.getExampleAsString()).isEqualTo("보기");
        assertThat(problem.getChoice1()).isEqualTo("선택1");
        assertThat(problem.getChoice2()).isEqualTo("선택2");
        assertThat(problem.getChoice3()).isEqualTo("선택3");
        assertThat(problem.getChoice4()).isEqualTo("선택4");
        assertThat(problem.getChoice5()).isEqualTo("선택5");
        assertThat(problem.getExplanationAsString()).isEqualTo("해설");
    }
    
    @Test
    @DisplayName("문제 변경")
    void testProblemModify() {
        // given
        Problem problem = ProblemFixture.MATH_DENSITY_2.get();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("entry", "언어");
        parameters.put("problemType", "언어유형1");
        parameters.put("question", "질문");
        parameters.put("example", "보기");
        parameters.put("choice1", "선택1");
        parameters.put("choice2", "선택2");
        parameters.put("choice3", "선택3");
        parameters.put("choice4", "선택4");
        parameters.put("choice5", "선택5");
        parameters.put("explanation", "해설");

        // when
        problem.modify(parameters);
    
        // then
        assertThat(problem.getEntry()).isEqualTo(Entry.LANGUAGE);
        assertThat(problem.getProblemType()).isEqualTo("언어유형1");
        assertThat(problem.getQuestionAsString()).isEqualTo("질문");
        assertThat(problem.getExampleAsString()).isEqualTo("보기");
        assertThat(problem.getChoice1()).isEqualTo("선택1");
        assertThat(problem.getChoice2()).isEqualTo("선택2");
        assertThat(problem.getChoice3()).isEqualTo("선택3");
        assertThat(problem.getChoice4()).isEqualTo("선택4");
        assertThat(problem.getChoice5()).isEqualTo("선택5");
        assertThat(problem.getExplanationAsString()).isEqualTo("해설");
    }
}