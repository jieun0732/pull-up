package pull_up.infra.database.repository.member;

import java.util.List;
import pull_up.infra.database.entity.MemberAnswer;

public interface CustomMemberAnswerRepository {
    List<MemberAnswer> findByMemberAndOptionalFilters(Long memberId, String entry, String category, String type);
}

