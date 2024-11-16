package pull_up.infra.database.repository.answer;

import pull_up.infra.database.entity.Answer;

import java.util.List;
import java.util.Optional;

public interface CustomAnswerRepository {
    List<Answer> findIncorrectAnswersByMemberId(Long memberId);

    List<Answer> findSolvedAnswersByMemberIdAndEntry(Long memberId, String entry);

    Optional<Answer> findByIdWithProblem(Long id);

    Optional<Answer> findByIdWithProblem(Long id, Boolean isSolved);

}
