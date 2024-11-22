package pull_up.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.jpa.entity.Examsheet;

public interface ExamsheetRepository extends JpaRepository <Examsheet, Long> {
    Examsheet findByExamTitle(String examTitle);
}
