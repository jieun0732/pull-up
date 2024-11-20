package pull_up.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.jpa.entity.Answer;

public interface AnswerRepository extends JpaRepository <Answer, Long> {

}
