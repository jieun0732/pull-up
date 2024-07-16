package pull_up.api.problem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.api.problem.entity.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByEntryAndCategoryAndType(String entry, String category, String type);
}
