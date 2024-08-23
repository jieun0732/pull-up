package pull_up.api.member.repository;

import java.util.List;
import pull_up.api.member.entity.MemberAnswer;

public interface MemberAnswerRepositoryCustom {
    List<MemberAnswer> findByMemberIdAndOptionalFilters(Long memberId, String entry, String category, String type);
}

