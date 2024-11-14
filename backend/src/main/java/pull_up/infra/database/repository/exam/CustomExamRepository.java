package pull_up.infra.database.repository.exam;

import pull_up.infra.database.entity.Exam;

import java.util.Optional;

public interface CustomExamRepository {
    Optional<Exam> findByIdWithAnswer(Long id);
}
