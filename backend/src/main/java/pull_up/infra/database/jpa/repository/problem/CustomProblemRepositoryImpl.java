package pull_up.infra.database.jpa.repository.problem;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Problem;
import pull_up.infra.database.jpa.entity.QProblem;
import pull_up.infra.database.jpa.entity.legacy.ProblemL;
import pull_up.infra.database.jpa.repository.legacy.ProblemLRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pull_up.infra.database.jpa.entity.QProblem.problem;
import static pull_up.infra.database.jpa.entity.legacy.QProblemL.problemL;


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
}
