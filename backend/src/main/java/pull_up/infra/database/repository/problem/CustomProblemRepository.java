package pull_up.infra.database.repository.problem;

import pull_up.api.problem.dto.ProblemDto;
import pull_up.infra.database.entity.legacy.ProblemL;

import java.util.List;

public interface CustomProblemRepository {
    List<ProblemDto> findByEntryAndCategory(String entry, String category);

    void deleteAllWithRelation();

    List<ProblemL> findByEntryAndCategoryAndType(String entry, String category, String type);
}
