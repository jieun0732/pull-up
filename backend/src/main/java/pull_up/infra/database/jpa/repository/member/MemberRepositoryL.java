package pull_up.infra.database.jpa.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.jpa.entity.legacy.MemberL;

import java.util.Optional;

/**
 * Member 레포지토리.
 */
public interface MemberRepositoryL extends JpaRepository<MemberL, Long>, CustomMemberRepository {

    Optional<MemberL> findByEmail(String email);

    Optional<MemberL> findByEmailAndRole(String email, String role);
}
