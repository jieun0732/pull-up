package pull_up.infra.database.jpa.repository.examsheet;

import pull_up.domain.examsheet.dto.ExamsheetDetailInfo;
import pull_up.infra.database.jpa.dto.ExamsheetInfo;

import java.util.List;
import java.util.Optional;

public interface CustomExamsheetRepository {
    List<ExamsheetInfo> searchExamsheet();

    Optional<ExamsheetDetailInfo> findByIdWithProblem(Long examsheetId);
}
