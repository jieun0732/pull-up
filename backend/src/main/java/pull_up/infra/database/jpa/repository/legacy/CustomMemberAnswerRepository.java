package pull_up.infra.database.jpa.repository.legacy;

import java.util.List;
import pull_up.infra.database.jpa.entity.legacy.MemberAnswer;

public interface CustomMemberAnswerRepository {
    List<MemberAnswer> findByMemberAndOptionalFilters(Long memberId, String entry, String category, String type);
}

