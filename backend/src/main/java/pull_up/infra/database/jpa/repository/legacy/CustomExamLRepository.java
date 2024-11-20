package pull_up.infra.database.jpa.repository.legacy;

import pull_up.infra.database.jpa.entity.legacy.ExamL;

import java.util.Optional;

public interface CustomExamLRepository {
    Optional<ExamL> findByIdWithAnswer(Long id);
}
