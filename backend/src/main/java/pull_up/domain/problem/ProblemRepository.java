package pull_up.domain.problem;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.legacy.ProblemL;
import pull_up.infra.database.repository.problem.CustomProblemRepository;

public interface ProblemRepository extends JpaRepository<ProblemL, Long>, CustomProblemRepository {

    List<ProblemL> findByCategory(String category);

    List<ProblemL> findByCategoryNot(String category);

    List<ProblemL> findByCategoryAndEntry(String category, String entryName);
}
