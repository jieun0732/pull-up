package pull_up.infra.database.repository.member;

import pull_up.infra.database.entity.Member;

public interface CustomMemberRepository {
    Member findMemberByIdWithRelation(Long id);
}
