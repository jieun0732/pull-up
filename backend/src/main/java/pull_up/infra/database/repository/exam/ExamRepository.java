package pull_up.infra.database.repository.exam;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long>, CustomExamRepository {

    Optional<Exam> findTopByMemberIdOrderByCreatedDateDesc(Long memberId);
}
