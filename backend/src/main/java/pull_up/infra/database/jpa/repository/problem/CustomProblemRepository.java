package pull_up.infra.database.jpa.repository.problem;

import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.dto.ProblemInfo;
import pull_up.infra.database.jpa.dto.SearchParam;

import java.util.List;
import java.util.Map;

public interface CustomProblemRepository {
    Map<String, Integer> findAllProblemTypeAndCountByEntry(Entry entry);
    List<ProblemInfo> searchProblem(SearchParam searchParam);
}
