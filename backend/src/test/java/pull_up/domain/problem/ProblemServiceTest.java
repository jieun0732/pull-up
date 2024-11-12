package pull_up.domain.problem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.api.problem.dto.CreateProblem;
import pull_up.global.dto.MessageDto;
import pull_up.infra.database.entity.Problem;
import pull_up.infra.database.repository.MockProblemRepository;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemServiceTest {

    ProblemService suit;

    ProblemRepository mockProblemRepository;

    @BeforeEach
    void init() {
        mockProblemRepository = new MockProblemRepository();
        suit = new ProblemService(mockProblemRepository);
    }

    @Test
    @DisplayName("문제 생성 테스트")
    void createProblemTest() {
        // given
        CreateProblem.Request request = new CreateProblem.Request("", "", "", "", "", "", "", "", "", "", "", "", 0D);

        // when
        MessageDto problem = suit.createProblem(request);

        // then
        assertThat(problem.message()).isEqualTo("Problem 1 has been created successfully.");
    }

    @Test
    @DisplayName("전체 문제 삭제 테스트")
    void testDeleteAll() {
        // given
        mockProblemRepository.save(Problem.of("", "", "", "", "", "", "", "", "", "", "", "", 0, 0, 0D));
        mockProblemRepository.save(Problem.of("", "", "", "", "", "", "", "", "", "", "", "", 0, 0, 0D));
        mockProblemRepository.save(Problem.of("", "", "", "", "", "", "", "", "", "", "", "", 0, 0, 0D));

        assertThat(mockProblemRepository.findAll()).hasSize(3);

        // when
        suit.deleteAllProblem();

        // then
        assertThat(mockProblemRepository.findAll()).hasSize(0);
    }
}