package pull_up.infra.database.jpa.repository.legacy;

import pull_up.api.problem.dto.ProblemDto;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.legacy.ProblemL;

import java.util.List;
import java.util.Map;

public interface CustomProblemLRepository {
    List<ProblemDto> findByEntryAndCategory(String entry, String category);

    List<ProblemL> findByEntryAndCategoryAndType(String entry, String category, String type);

    Map<String, Integer> findAllProblemTypeAndCountByEntry(Entry entry);
}
