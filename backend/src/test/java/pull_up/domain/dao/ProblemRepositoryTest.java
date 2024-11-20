package pull_up.domain.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.problem.Entry;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class ProblemRepositoryTest {

    @Autowired
    ProblemRepository suit;

    @Test
    @DisplayName("DB에 저장된 해당 영역의 모든 문제 타입 가져오기 테스트")
    void testFindAllProblemTypeAndCount() {
        // given
        Entry entry = Entry.MATH;

        // when
        Map<String, Integer> allInDB = suit.findAllProblemTypeAndCountByEntry(entry);

        // then
        assertThat(allInDB).hasSize(2);
    }
}