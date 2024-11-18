package pull_up.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.entity.Problem;
import pull_up.infra.database.repository.problem.CustomProblemRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long>, CustomProblemRepository {

    List<Problem> findAllByEntry(Entry entry);
}
