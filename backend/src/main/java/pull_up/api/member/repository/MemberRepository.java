package pull_up.api.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.api.member.entity.Member;

import java.util.Optional;

/**
 * Member 레포지토리.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndRole(String email, String role);
}
