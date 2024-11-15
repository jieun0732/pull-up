package pull_up.infra.database.repository.legacy;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.entity.legacy.IncorrectAnswer;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.entity.Problem;

public interface IncorrectAnswerRepository extends JpaRepository<IncorrectAnswer, Long> {

    List<IncorrectAnswer> findByMemberId(Long memberId);

    Optional<IncorrectAnswer> findByMemberAndProblemAndExam(Member member, Problem problem,
                                                            Exam exam);

    Optional<IncorrectAnswer> findByMemberAndProblem(Member member, Problem problem);
}
