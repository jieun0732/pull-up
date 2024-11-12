package pull_up.infra.database.repository.member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.ExamInformation;
import pull_up.infra.database.entity.IncorrectAnswer;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.entity.Problem;

public interface IncorrectAnswerRepository extends JpaRepository<IncorrectAnswer, Long> {

    List<IncorrectAnswer> findByMemberId(Long memberId);

    Optional<IncorrectAnswer> findByMemberAndProblemAndExamInformation(Member member, Problem problem,
        ExamInformation examInformation);

    Optional<IncorrectAnswer> findByMemberAndProblem(Member member, Problem problem);
}
