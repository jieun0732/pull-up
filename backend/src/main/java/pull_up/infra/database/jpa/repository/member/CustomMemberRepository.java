package pull_up.infra.database.jpa.repository.member;

import pull_up.infra.database.jpa.dto.IncorrectQueryDto;

import java.util.List;

public interface CustomMemberRepository {
    List<IncorrectQueryDto> getIncorrectAnswersById(Long memberId);
}
