package pull_up.domain.problem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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