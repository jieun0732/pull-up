package pull_up.infra.database.repository.answer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long>, CustomAnswerRepository {

    List<Answer> findByExamId(Long examId);

    Answer findByExamIdAndProblemNumber(Long examInformationId, Long problemNumber);
}
