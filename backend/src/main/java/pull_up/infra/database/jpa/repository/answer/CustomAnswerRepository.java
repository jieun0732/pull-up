package pull_up.infra.database.jpa.repository.answer;

import pull_up.infra.database.jpa.entity.legacy.AnswerL;

import java.util.List;
import java.util.Optional;

public interface CustomAnswerRepository {
    List<AnswerL> findIncorrectAnswersByMemberId(Long memberId);

    List<AnswerL> findSolvedAnswersByMemberIdAndEntry(Long memberId, String entry);

    Optional<AnswerL> findByIdWithProblem(Long id);

    Optional<AnswerL> findByIdWithProblem(Long id, Boolean isSolved);

}
