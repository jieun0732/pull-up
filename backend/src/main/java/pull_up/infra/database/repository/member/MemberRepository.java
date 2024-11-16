package pull_up.infra.database.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.legacy.MemberL;

import java.util.Optional;

/**
 * Member 레포지토리.
 */
public interface MemberRepository extends JpaRepository<MemberL, Long>, CustomMemberRepository {

    Optional<MemberL> findByEmail(String email);

    Optional<MemberL> findByEmailAndRole(String email, String role);
}
