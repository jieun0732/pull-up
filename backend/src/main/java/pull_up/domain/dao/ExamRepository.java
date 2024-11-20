package pull_up.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.repository.exam.CustomExamRepository;

public interface ExamRepository extends JpaRepository<Exam, Long>, CustomExamRepository {

}
