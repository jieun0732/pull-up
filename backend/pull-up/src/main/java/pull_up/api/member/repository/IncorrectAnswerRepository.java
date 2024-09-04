package pull_up.api.member.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.member.entity.IncorrectAnswer;
import pull_up.api.member.entity.Member;
import pull_up.api.problem.entity.Problem;

public interface IncorrectAnswerRepository extends JpaRepository<IncorrectAnswer, Long> {

    List<IncorrectAnswer> findByMemberId(Long memberId);

    Optional<IncorrectAnswer> findByMemberAndProblemAndExamInformation(Member member, Problem problem,
        ExamInformation examInformation);

    Optional<IncorrectAnswer> findByMemberAndProblem(Member member, Problem problem);
}
