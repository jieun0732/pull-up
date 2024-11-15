package pull_up.infra.database.repository.legacy;

import java.util.List;
import pull_up.infra.database.entity.legacy.MemberAnswer;

public interface CustomMemberAnswerRepository {
    List<MemberAnswer> findByMemberAndOptionalFilters(Long memberId, String entry, String category, String type);
}

