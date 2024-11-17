package pull_up.infra.database.repository.problem;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.legacy.ProblemL;

import java.util.List;

public interface ProblemRepositoryL extends JpaRepository<ProblemL, Long>, CustomProblemRepository {

    public List<ProblemL> findByCategoryNot(String category);

    List<ProblemL> findByCategory(String category);

    List<ProblemL> findByCategoryAndEntry(String category, String entryName);
}
