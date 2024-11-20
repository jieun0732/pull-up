package pull_up.infra.database.jpa.repository.legacy;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.jpa.entity.legacy.ProblemL;
import pull_up.infra.database.jpa.repository.problem.CustomProblemRepository;

import java.util.List;

public interface ProblemLRepository extends JpaRepository<ProblemL, Long>, CustomProblemLRepository {

    public List<ProblemL> findByCategoryNot(String category);

    List<ProblemL> findByCategory(String category);

    List<ProblemL> findByCategoryAndEntry(String category, String entryName);
}
