package pull_up.infra.database.jpa.repository.problem;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import pull_up.domain.problem.Entry;
import pull_up.domain.problem.dto.ProblemInfo;
import pull_up.domain.problem.dto.QProblemInfo;
import pull_up.infra.database.jpa.dto.SearchParam;
import pull_up.infra.database.jpa.embedded.Problemsheet;
import pull_up.infra.database.jpa.entity.Examsheet;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pull_up.infra.database.jpa.embedded.QProblemsheet.problemsheet;
import static pull_up.infra.database.jpa.entity.QExamsheet.examsheet;
import static pull_up.infra.database.jpa.entity.QProblem.problem;
import static pull_up.infra.database.jpa.repository.DynamicQuery.*;


@Repository
public class CustomProblemRepositoryImpl implements CustomProblemRepository {

    private final JPAQueryFactory qf;

    public CustomProblemRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public Map<String, Integer> findAllProblemTypeAndCountExceptProblemsheetByEntry(Entry entry) {
        List<Problem> problems = qf.selectFrom(problem)
                .where(problem.entry.eq(entry))
                .fetch();

        List<Long> exceptProblemId = getProblemIdInProblemsheet();

        Map<String, Integer> ret = new HashMap<>();
        for (Problem p : problems) {
            String problemTypeKey = p.getProblemType();
            if (exceptProblemId.contains(p.getId())) continue;
            if (!ret.containsKey(problemTypeKey)) ret.put(problemTypeKey, 1);
            else ret.put(problemTypeKey, ret.get(problemTypeKey) + 1);
        }

        return ret;
    }

    private List<Long> getProblemIdInProblemsheet() {
        return qf.selectFrom(examsheet)
                .leftJoin(examsheet.problemsheets, problemsheet).fetchJoin()
                .fetch()
                .stream().map(Examsheet::getProblemsheets).flatMap(List::stream)
                .map(Problemsheet::getProblemId).toList();
    }

    @Override
    public List<Problem> findAllByEntryAndProblemTypeExceptProblemsheet(Entry entry, String problemType) {
        List<Problem> problems = qf.selectFrom(problem)
                .where(problem.entry.eq(entry)
                        .and(problem.problemType.equalsIgnoreCase(problemType)))
                .fetch();

        List<Long> exceptProblemId = getProblemIdInProblemsheet();

        return problems.stream().filter(p -> !exceptProblemId.contains(p.getId())).toList();
    }

    @Override
    public Page<ProblemInfo> searchProblem(SearchParam searchParam) {
        List<ProblemInfo> problemInfos = qf.select(new QProblemInfo(
                        problem.id,
                        problem.entry,
                        problem.problemType,
                        problem.createdTime,
                        problem.updatedTime,
                        problem.questionSummary,
                        problem.totalAttempts,
                        problem.incorrectRate))
                .from(problem)
                .where(problemSearch(searchParam))
                .orderBy(problemOrder(searchParam))
                .orderBy(problem.id.desc())
                .offset(searchParam.pageable().getOffset())
                .limit(searchParam.pageable().getPageSize())
                .fetch();

        Long count = qf.select(problem.count())
                .from(problem)
                .where(problemSearch(searchParam))
                .orderBy(problemOrder(searchParam))
                .fetchFirst();

        return new PageImpl<>(problemInfos, searchParam.pageable(), count);
    }}
