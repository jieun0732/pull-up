package pull_up.infra.database.jpa.repository.problem;

import pull_up.domain.problem.Entry;

import java.util.Map;

public interface CustomProblemRepository {
    Map<String, Integer> findAllProblemTypeAndCountByEntry(Entry entry);
}
