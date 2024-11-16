package pull_up.infra.database.repository.member;

import pull_up.infra.database.entity.legacy.MemberL;

public interface CustomMemberRepository {
    MemberL findMemberByIdWithRelation(Long id);
}
