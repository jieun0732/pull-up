package pull_up.infra.database.jpa.repository.member;

import pull_up.infra.database.jpa.entity.Member;

import java.util.Optional;

public interface CustomMemberRepository {
    Optional<Member> findMemberSolvedInfo(Long id);
}
