package pull_up.api.member.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.api.member.entity.IncorrectAnswer;

public interface IncorrectAnswerRepository extends JpaRepository<IncorrectAnswer, Long> {

    List<IncorrectAnswer> findByMemberId(Long memberId);
}
