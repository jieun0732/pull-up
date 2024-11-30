package pull_up.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.jpa.entity.Examsheet;
import pull_up.infra.database.jpa.repository.examsheet.CustomExamsheetRepository;

public interface ExamsheetRepository extends JpaRepository <Examsheet, Long>, CustomExamsheetRepository {
    Examsheet findByExamTitle(String examTitle);
}
