package pull_up.infra.database.jpa.repository.answer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.jpa.entity.legacy.AnswerL;

public interface AnswerRepositoryL extends JpaRepository<AnswerL, Long>, CustomAnswerRepository {

    List<AnswerL> findByExamLId(Long examId);

    AnswerL findByExamLIdAndProblemNumber(Long examInformationId, Long problemNumber);
}
