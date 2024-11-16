package pull_up.domain.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {
}
