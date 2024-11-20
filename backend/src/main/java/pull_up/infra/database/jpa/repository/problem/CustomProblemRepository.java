package pull_up.infra.database.jpa.repository.problem;

import pull_up.api.problem.dto.ProblemDto;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.legacy.ProblemL;

import java.util.List;
import java.util.Map;

public interface CustomProblemRepository {
    Map<String, Integer> findAllProblemTypeAndCountByEntry(Entry entry);
}
