package pull_up.infra.database.repository.exam;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.ExamInformation;

public interface ExamInformationRepository extends JpaRepository<ExamInformation, Long> {

    Optional<ExamInformation> findTopByMemberIdOrderByCreatedDateDesc(Long memberId);
}
