package pull_up.infra.database.jpa.repository.exam;

import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.List;
import java.util.Map;

public interface CustomExamRepository {
    Map<String, Exam> findAllEvenlyAndProblemTypeExamMap(Long memberId, Entry entry);
}
