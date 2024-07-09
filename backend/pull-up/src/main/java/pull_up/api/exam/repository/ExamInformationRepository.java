package pull_up.api.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.api.exam.entity.ExamInformation;

public interface ExamInformationRepository extends JpaRepository<ExamInformation, Long> {

}
