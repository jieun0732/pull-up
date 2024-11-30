package pull_up.infra.database.jpa.repository.examsheet;

import pull_up.infra.database.jpa.dto.ExamsheetInfo;

import java.util.List;

public interface CustomExamsheetRepository {
    List<ExamsheetInfo> searchExamsheet();
}
