package pull_up.api.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.api.member.entity.MemberAnswer;

public interface MemberAnswerRepository extends JpaRepository<MemberAnswer, Long> {

}
