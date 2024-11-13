package pull_up.infra.database.repository.exam;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.AnsweredProblem;

public interface AnsweredProblemRepository extends JpaRepository<AnsweredProblem, Long> {

    List<AnsweredProblem> findByExamId(Long examId);

    AnsweredProblem findByExamIdAndProblemNumber(Long examInformationId, Long problemNumber);
}
