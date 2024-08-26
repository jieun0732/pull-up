package pull_up.api.problem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.api.problem.entity.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<ProblemDto> findByEntryAndCategory(String entry, String category);

    @Query("SELECT p FROM Problem p WHERE p.category = :category AND p.isDeleted = false")
    List<Problem> findByCategory(@Param("category") String category);

    List<Problem> findByCategoryNot(String category);
}
