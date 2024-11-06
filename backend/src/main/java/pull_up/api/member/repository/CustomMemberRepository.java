package pull_up.api.member.repository;

import pull_up.api.member.entity.Member;

public interface CustomMemberRepository {
    Member findMemberByIdWithRelation(Long id);
}
