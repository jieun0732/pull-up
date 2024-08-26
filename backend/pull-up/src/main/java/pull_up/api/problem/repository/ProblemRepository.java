package pull_up.api.problem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.api.problem.entity.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<ProblemDto> findByEntryAndCategory(String entry, String category);

    List<Problem> findByCategory(String category);

    List<Problem> findByCategoryNot(String category);

    List<Problem> findByCategoryAndEntry(String category, String entryName);
}
