package pull_up.infra.database.jpa.repository.answer;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pull_up.infra.database.jpa.entity.legacy.AnswerL;

import java.util.List;
import java.util.Optional;

import static pull_up.infra.database.jpa.entity.legacy.QAnswerL.answerL;
import static pull_up.infra.database.jpa.entity.legacy.QProblemL.problemL;

public class CustomAnswerRepositoryImpl implements CustomAnswerRepository {

    private final JPAQueryFactory qf;

    public CustomAnswerRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public List<AnswerL> findIncorrectAnswersByMemberId(Long memberId) {
        return qf.selectFrom(answerL)
                .where(answerL.examL.memberL.id.eq(memberId)
                        .and(answerL.chosenAnswer.ne(""))
                        .and(answerL.isCorrect.eq(false)))
                .fetch();
    }

    @Override
    public List<AnswerL> findSolvedAnswersByMemberIdAndEntry(Long memberId, String entry) {
        return List.of();
    }

    @Override
    public Optional<AnswerL> findByIdWithProblem(Long id) {
        return Optional.ofNullable(
                qf.selectFrom(answerL)
                    .leftJoin(answerL.problemL, problemL).fetchJoin()
                    .where(answerL.id.eq(id)).fetchFirst()
        );
    }

    @Override
    public Optional<AnswerL> findByIdWithProblem(Long id, Boolean isSolved) {
        if (!isSolved) return findByIdWithProblem(id);

        return Optional.ofNullable(
                qf.selectFrom(answerL)
                        .leftJoin(answerL.problemL, problemL).fetchJoin()
                        .where(answerL.id.eq(id)
                                .and(answerL.isSolved.eq(true))).fetchFirst()
        );
    }
}
