package pull_up.infra.database.repository.problem;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.infra.database.entity.legacy.ProblemL;

import java.util.List;

import static pull_up.infra.database.entity.legacy.QAnswerL.answerL;
import static pull_up.infra.database.entity.legacy.QProblemL.problemL;
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
        List<ProblemL> problemLS = qf.selectFrom(problemL)
                .where(problemL.entry.eq(entry)
                        .and(problemL.category.eq(category))).fetch();
        return problemLS.stream().map(ProblemDto::toDto).toList();
    }

    @Override
    public void deleteAllWithRelation() {
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

        qf.delete(problemL).execute();
        qf.delete(answerL).execute();
        qf.delete(memberAnswer).execute();
        qf.delete(incorrectAnswer).execute();

        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    @Override
    public List<ProblemL> findByEntryAndCategoryAndType(String entry, String category, String type) {
        return qf.selectFrom(problemL)
                .where(entryQuery(entry),
                        categoryQuery(category),
                        typeQuery(type))
                .fetch();
    }

    private BooleanExpression entryQuery(String entry) {
        if (entry == null || entry.isBlank()) return null;
        return problemL.entry.eq(entry);
    }

    private BooleanExpression categoryQuery(String category) {
        if (category == null || category.isBlank()) return null;
        return problemL.category.eq(category);
    }

    private BooleanExpression typeQuery(String type) {
        if (type == null || type.isBlank()) return null;
        return problemL.type.eq(type);
    }
}
