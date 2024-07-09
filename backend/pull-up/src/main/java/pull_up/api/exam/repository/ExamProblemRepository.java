package pull_up.api.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.api.exam.entity.ExamProblem;

public interface ExamProblemRepository extends JpaRepository<ExamProblem, Long> {

}
