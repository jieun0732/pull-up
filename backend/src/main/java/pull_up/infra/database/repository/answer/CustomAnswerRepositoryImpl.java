package pull_up.infra.database.repository.answer;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pull_up.infra.database.entity.Answer;
import pull_up.infra.database.entity.QAnswer;
import pull_up.infra.database.entity.QProblem;

import java.util.List;
import java.util.Optional;

import static pull_up.infra.database.entity.QAnswer.answer;
import static pull_up.infra.database.entity.QProblem.problem;

public class CustomAnswerRepositoryImpl implements CustomAnswerRepository {

    private final JPAQueryFactory qf;

    public CustomAnswerRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public List<Answer> findIncorrectAnswersByMemberId(Long memberId) {
        return qf.selectFrom(answer)
                .where(answer.exam.member.id.eq(memberId)
                        .and(answer.chosenAnswer.ne(""))
                        .and(answer.isCorrect.eq(false)))
                .fetch();
    }

    @Override
    public Optional<Answer> findByIdWithProblem(Long id) {
        return Optional.ofNullable(
                qf.selectFrom(answer)
                    .leftJoin(answer.problem, problem).fetchJoin()
                    .where(answer.id.eq(id)).fetchFirst()
        );
    }

    @Override
    public Optional<Answer> findByIdWithProblem(Long id, Boolean isSolved) {
        if (!isSolved) return findByIdWithProblem(id);

        return Optional.ofNullable(
                qf.selectFrom(answer)
                        .leftJoin(answer.problem, problem).fetchJoin()
                        .where(answer.id.eq(id)
                                .and(answer.isSolved.eq(true))).fetchFirst()
        );
    }
}
