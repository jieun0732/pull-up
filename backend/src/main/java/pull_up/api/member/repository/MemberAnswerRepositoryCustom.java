package pull_up.api.member.repository;

import java.util.List;
import pull_up.api.member.entity.MemberAnswer;

public interface MemberAnswerRepositoryCustom {
    List<MemberAnswer> findByMemberAndOptionalFilters(Long memberId, String entry, String category, String type);
}

