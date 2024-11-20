package pull_up.infra.database.jpa.repository.legacy;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.jpa.entity.legacy.ExamL;
import pull_up.infra.database.jpa.entity.legacy.IncorrectAnswer;
import pull_up.infra.database.jpa.entity.legacy.MemberL;
import pull_up.infra.database.jpa.entity.legacy.ProblemL;

public interface IncorrectAnswerRepository extends JpaRepository<IncorrectAnswer, Long> {

    Optional<IncorrectAnswer> findByMemberLAndProblemLAndExamL(MemberL memberL, ProblemL problemL, ExamL examL);

    Optional<IncorrectAnswer> findByMemberLAndProblemL(MemberL memberL, ProblemL problemL);
}
