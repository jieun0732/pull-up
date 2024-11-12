package pull_up.infra.database.repository.problem;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.infra.database.entity.Problem;

import java.util.List;

import static pull_up.infra.database.entity.QProblem.problem;


@Repository
public class CustomProblemRepositoryImpl implements CustomProblemRepository {

    private final JPAQueryFactory qf;

    public CustomProblemRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public List<ProblemDto> findByEntryAndCategory(String entry, String category) {
        List<Problem> problems = qf.selectFrom(problem)
                .where(problem.entry.eq(entry)
                        .and(problem.category.eq(category))).fetch();
        return problems.stream().map(ProblemDto::from).toList();
    }
}
