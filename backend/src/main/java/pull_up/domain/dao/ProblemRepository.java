package pull_up.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Problem;
import pull_up.infra.database.jpa.repository.problem.CustomProblemRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long>, CustomProblemRepository {

    List<Problem> findAllByEntry(Entry entry);

    List<Problem> findAllByEntryAndProblemType(Entry entry, String problemType);
}
