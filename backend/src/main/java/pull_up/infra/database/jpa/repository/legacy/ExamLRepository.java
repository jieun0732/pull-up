package pull_up.infra.database.jpa.repository.legacy;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.jpa.entity.legacy.ExamL;

public interface ExamLRepository extends JpaRepository<ExamL, Long>, CustomExamLRepository {

    Optional<ExamL> findTopByMemberLIdOrderByCreatedDateDesc(Long memberId);
}
