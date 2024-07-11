package pull_up.api.exam.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.exam.entity.ExamProblem;

public interface ExamProblemRepository extends JpaRepository<ExamProblem, Long> {

    List<ExamProblem> findByExamInformation(ExamInformation examInformation);

    List<ExamProblem> findByExamInformationId(Long examId);
}
