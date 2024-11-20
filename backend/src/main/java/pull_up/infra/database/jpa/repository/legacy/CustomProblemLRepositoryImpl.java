package pull_up.infra.database.jpa.repository.legacy;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.legacy.ProblemL;

import java.util.List;
import java.util.Map;

import static pull_up.infra.database.jpa.entity.legacy.QProblemL.problemL;


@Repository
public class CustomProblemLRepositoryImpl implements CustomProblemLRepository {

    private final JPAQueryFactory qf;
    private final EntityManager em;

    public CustomProblemLRepositoryImpl(EntityManager em) {
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
    public List<ProblemL> findByEntryAndCategoryAndType(String entry, String category, String type) {
        return qf.selectFrom(problemL)
                .where(entryQuery(entry),
                        categoryQuery(category),
                        typeQuery(type))
                .fetch();
    }

    @Override
    public Map<String, Integer> findAllProblemTypeAndCountByEntry(Entry entry) {
        return Map.of();
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
