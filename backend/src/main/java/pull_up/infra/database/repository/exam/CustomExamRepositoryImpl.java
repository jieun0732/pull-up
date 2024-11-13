package pull_up.infra.database.repository.exam;

import org.springframework.stereotype.Repository;
import pull_up.infra.database.entity.Exam;

import java.util.Optional;

public class CustomExamRepositoryImpl implements CustomExamRepository{

    @Override
    public Optional<Exam> findByIdWithAnswer(Long id) {
        return Optional.empty();
    }
}
