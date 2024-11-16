package pull_up.infra.database.repository.answer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.legacy.AnswerL;

public interface AnswerRepository extends JpaRepository<AnswerL, Long>, CustomAnswerRepository {

    List<AnswerL> findByExamLId(Long examId);

    AnswerL findByExamLIdAndProblemNumber(Long examInformationId, Long problemNumber);
}
