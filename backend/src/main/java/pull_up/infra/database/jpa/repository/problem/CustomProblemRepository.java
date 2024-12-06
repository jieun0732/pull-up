package pull_up.infra.database.jpa.repository.problem;

import org.springframework.data.domain.Page;
import pull_up.domain.problem.Entry;
import pull_up.domain.problem.dto.ProblemInfo;
import pull_up.infra.database.jpa.dto.SearchParam;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.List;
import java.util.Map;

public interface CustomProblemRepository {
    Map<String, Integer> findAllProblemTypeAndCountExceptProblemsheetByEntry(Entry entry);

    Page<ProblemInfo> searchProblem(SearchParam searchParam);

    List<Problem> findAllByEntryAndProblemTypeExceptProblemsheet(Entry entry, String problemType);
}
