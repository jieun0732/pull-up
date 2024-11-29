package pull_up.infra.database.jpa.repository.problem;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.dto.ProblemInfo;
import pull_up.infra.database.jpa.dto.QProblemInfo;
import pull_up.infra.database.jpa.dto.SearchParam;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pull_up.infra.database.jpa.entity.QProblem.problem;


@Repository
public class CustomProblemRepositoryImpl implements CustomProblemRepository {

    private final JPAQueryFactory qf;

    public CustomProblemRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public Map<String, Integer> findAllProblemTypeAndCountByEntry(Entry entry) {
        List<Problem> problems = qf.selectFrom(problem)
                .where(problem.entry.eq(entry))
                .fetch();

        Map<String, Integer> ret = new HashMap<>();
        for (Problem p : problems) {
            String problemTypeKey = p.getProblemType();
            if (!ret.containsKey(problemTypeKey)) ret.put(problemTypeKey, 1);
            else ret.put(problemTypeKey, ret.get(problemTypeKey) + 1);
        }

        return ret;
    }

    @Override
    public Page<ProblemInfo> searchProblem(SearchParam searchParam) {
        List<ProblemInfo> problemInfos = qf.select(new QProblemInfo(
                        problem.id,
                        problem.entry,
                        problem.problemType,
                        problem.createdTime,
                        problem.questionSummary,
                        problem.totalAttempts,
                        problem.incorrectRate))
                .from(problem)
                .where(search(searchParam))
                .orderBy(order(searchParam))
                .orderBy(problem.id.desc())
                .offset(searchParam.pageable().getOffset())
                .limit(searchParam.pageable().getPageSize())
                .fetch();

        Long count = qf.select(problem.count())
                .from(problem)
                .where(search(searchParam))
                .orderBy(order(searchParam))
                .fetchFirst();

        return new PageImpl<>(problemInfos, searchParam.pageable(), count);
    }

    private OrderSpecifier<?> order(SearchParam searchParam) {
        return switch (searchParam.sortType()) {
            case ID -> new OrderSpecifier<>(searchParam.sortOrder(), problem.id);
            case ENTRY -> new OrderSpecifier<>(searchParam.sortOrder(), problem.entry);
            case PROBLEM_TYPE -> new OrderSpecifier<>(searchParam.sortOrder(), problem.problemType);
            case CREATED_DATE -> new OrderSpecifier<>(searchParam.sortOrder(), problem.createdTime);
            case ATTEMPT -> new OrderSpecifier<>(searchParam.sortOrder(), problem.totalAttempts);
            case CORRECT_RATE -> new OrderSpecifier<>(searchParam.sortOrder(), problem.incorrectRate);
        };
    }

    private BooleanExpression search(SearchParam searchParam) {
        if (searchParam.keyword().equals("EMPTY")) return null;
        return switch (searchParam.searchType()) {
            case NONE -> null;
            case PROBLEM_TYPE -> problem.problemType.likeIgnoreCase("%" + searchParam.keyword() + "%");
            case QUESTION -> problem.questionSummary.likeIgnoreCase("%" + searchParam.keyword() + "%");
        };
    }
}
