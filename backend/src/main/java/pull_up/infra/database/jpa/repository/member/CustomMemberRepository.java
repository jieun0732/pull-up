package pull_up.infra.database.jpa.repository.member;

import org.springframework.data.domain.Page;
import pull_up.domain.member.dto.MemberListInfo;
import pull_up.infra.database.jpa.dto.IncorrectQueryDto;
import pull_up.infra.database.jpa.dto.SearchParam;

import java.util.List;

public interface CustomMemberRepository {
    List<IncorrectQueryDto> getIncorrectAnswersById(Long memberId);
    Page<MemberListInfo> searchMember(SearchParam searchParam);
}
