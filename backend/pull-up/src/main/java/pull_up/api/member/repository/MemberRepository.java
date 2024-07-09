package pull_up.api.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.api.member.entity.Member;

/**
 * Member 레포지토리.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

}
