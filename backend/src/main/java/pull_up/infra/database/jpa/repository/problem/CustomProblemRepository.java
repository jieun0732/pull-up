package pull_up.infra.database.jpa.repository.problem;

import org.springframework.data.domain.Page;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.dto.ProblemInfo;
import pull_up.infra.database.jpa.dto.SearchParam;

import java.util.List;
import java.util.Map;

public interface CustomProblemRepository {
    Map<String, Integer> findAllProblemTypeAndCountByEntry(Entry entry);
    Page<ProblemInfo> searchProblem(SearchParam searchParam);
}
