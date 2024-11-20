package pull_up.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.repository.member.CustomMemberRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
    Optional<Member> findBySnsId(String id);
}
