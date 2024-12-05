package pull_up.infra.database.jpa.repository.examsheet;

import pull_up.domain.examsheet.dto.ExamsheetInfo;
import pull_up.infra.database.jpa.embedded.Problemsheet;
import pull_up.infra.database.jpa.entity.Examsheet;

import java.util.List;
import java.util.Optional;

public interface CustomExamsheetRepository {
    List<ExamsheetInfo> searchExamsheet();

    Optional<Examsheet> findByIdWithProblem(Long examsheetId);

    List<Problemsheet> findAllProblemIdInProblemsheet();
}
