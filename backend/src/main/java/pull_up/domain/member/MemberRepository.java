package pull_up.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.repository.member.CustomMemberRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
    Optional<Member> findBySnsId(String id);
}
