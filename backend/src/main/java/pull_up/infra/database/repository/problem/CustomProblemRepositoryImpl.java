package pull_up.infra.database.repository.problem;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.infra.database.entity.Problem;

import java.util.List;

import static pull_up.infra.database.entity.QAnswer.answer;
import static pull_up.infra.database.entity.QProblem.problem;
import static pull_up.infra.database.entity.legacy.QIncorrectAnswer.incorrectAnswer;
import static pull_up.infra.database.entity.legacy.QMemberAnswer.memberAnswer;


@Repository
public class CustomProblemRepositoryImpl implements CustomProblemRepository {

    private final JPAQueryFactory qf;
    private final EntityManager em;

    public CustomProblemRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public List<ProblemDto> findByEntryAndCategory(String entry, String category) {
        List<Problem> problems = qf.selectFrom(problem)
                .where(problem.entry.eq(entry)
                        .and(problem.category.eq(category))).fetch();
        return problems.stream().map(ProblemDto::toDto).toList();
    }

    @Override
    public void deleteAllWithRelation() {
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

        qf.delete(problem).execute();
        qf.delete(answer).execute();
        qf.delete(memberAnswer).execute();
        qf.delete(incorrectAnswer).execute();

        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    @Override
    public List<Problem> findByEntryAndCategoryAndType(String entry, String category, String type) {
        return qf.selectFrom(problem)
                .where(entryQuery(entry),
                        categoryQuery(category),
                        typeQuery(type))
                .fetch();
    }

    private BooleanExpression entryQuery(String entry) {
        if (entry == null || entry.isBlank()) return null;
        return problem.entry.eq(entry);
    }

    private BooleanExpression categoryQuery(String category) {
        if (category == null || category.isBlank()) return null;
        return problem.category.eq(category);
    }

    private BooleanExpression typeQuery(String type) {
        if (type == null || type.isBlank()) return null;
        return problem.type.eq(type);
    }
}
