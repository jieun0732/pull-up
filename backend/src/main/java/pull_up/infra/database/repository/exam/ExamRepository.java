package pull_up.infra.database.repository.exam;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.legacy.ExamL;

public interface ExamRepository extends JpaRepository<ExamL, Long>, CustomExamRepository {

    Optional<ExamL> findTopByMemberLIdOrderByCreatedDateDesc(Long memberId);
}
