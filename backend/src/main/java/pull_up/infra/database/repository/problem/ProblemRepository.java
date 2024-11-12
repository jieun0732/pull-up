package pull_up.infra.database.repository.problem;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long>, CustomProblemRepository {

    List<Problem> findByCategory(String category);

    List<Problem> findByCategoryNot(String category);

    List<Problem> findByCategoryAndEntry(String category, String entryName);
}
