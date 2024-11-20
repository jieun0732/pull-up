package pull_up.infra.database.jpa.repository.exam;

import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.List;

public interface CustomExamRepository {
    List<Exam> findAllByMemberIdAndEntry(Long memberId, Entry entry);
}
