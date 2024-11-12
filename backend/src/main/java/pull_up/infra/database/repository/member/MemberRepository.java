package pull_up.infra.database.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.Member;

import java.util.Optional;

/**
 * Member 레포지토리.
 */
public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndRole(String email, String role);
}
