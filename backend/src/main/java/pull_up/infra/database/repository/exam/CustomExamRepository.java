package pull_up.infra.database.repository.exam;

import pull_up.infra.database.entity.legacy.ExamL;

import java.util.Optional;

public interface CustomExamRepository {
    Optional<ExamL> findByIdWithAnswer(Long id);
}
