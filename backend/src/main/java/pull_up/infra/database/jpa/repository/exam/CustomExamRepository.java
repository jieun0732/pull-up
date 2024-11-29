package pull_up.infra.database.jpa.repository.exam;

import pull_up.domain.member.dto.SolvedInfo;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.Map;
import java.util.Optional;

public interface CustomExamRepository {
    Map<String, Exam> findAllEvenlyAndProblemTypeExamMap(Long memberId, Entry entry);

    Optional<Exam> findMockExamByMemberId(Long memberId);

    SolvedInfo.MockExamResponse findSolvedMockExamInfo(Long memberId);
}
