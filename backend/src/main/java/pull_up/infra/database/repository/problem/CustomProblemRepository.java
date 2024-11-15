package pull_up.infra.database.repository.problem;

import pull_up.api.problem.dto.ProblemDto;
import pull_up.infra.database.entity.Problem;

import java.util.List;

public interface CustomProblemRepository {
    List<ProblemDto> findByEntryAndCategory(String entry, String category);

    void deleteAllWithRelation();

    List<Problem> findByEntryAndCategoryAndType(String entry, String category, String type);
}
