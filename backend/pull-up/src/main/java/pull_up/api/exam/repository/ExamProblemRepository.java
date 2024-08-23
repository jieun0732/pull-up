package pull_up.api.exam.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.exam.entity.ExamProblem;
import pull_up.api.problem.entity.Problem;

public interface ExamProblemRepository extends JpaRepository<ExamProblem, Long> {

    List<ExamProblem> findByExamInformationId(Long examId);

    ExamProblem findByExamInformationIdAndProblemNumber(Long examInformationId, Long problemNumber);
}
