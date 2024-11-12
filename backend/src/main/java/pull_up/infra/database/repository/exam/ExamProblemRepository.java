package pull_up.infra.database.repository.exam;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.ExamProblem;

public interface ExamProblemRepository extends JpaRepository<ExamProblem, Long> {

    List<ExamProblem> findByExamInformationId(Long examId);

    ExamProblem findByExamInformationIdAndProblemNumber(Long examInformationId, Long problemNumber);
}
