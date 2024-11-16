package pull_up.domain.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.Answer;

public interface AnswerRepository extends JpaRepository <Answer, Long> {

}
