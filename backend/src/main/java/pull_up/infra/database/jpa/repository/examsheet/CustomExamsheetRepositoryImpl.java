package pull_up.infra.database.jpa.repository.examsheet;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pull_up.domain.examsheet.dto.ExamsheetDetailInfo;
import pull_up.infra.database.jpa.dto.ExamsheetInfo;
import pull_up.infra.database.jpa.embedded.Problemsheet;
import pull_up.infra.database.jpa.entity.Examsheet;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.*;

import static pull_up.infra.database.jpa.embedded.QProblemsheet.problemsheet;
import static pull_up.infra.database.jpa.entity.QExamsheet.examsheet;
import static pull_up.infra.database.jpa.entity.QProblem.problem;

public class CustomExamsheetRepositoryImpl implements CustomExamsheetRepository {

    private final JPAQueryFactory qf;

    public CustomExamsheetRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public List<ExamsheetInfo> searchExamsheet() {
        List<Examsheet> examsheets = qf.selectFrom(examsheet)
                .leftJoin(examsheet.problemsheets, problemsheet).fetchJoin()
                .fetch();

        List<ExamsheetInfo> ret = new ArrayList<>();
        for (Examsheet e : examsheets) {
            ret.add(ExamsheetInfo.toDto(e));
        }
        return ret;
    }

    @Override
    public Optional<Examsheet> findByIdWithProblem(Long examsheetId) {
        return Optional.ofNullable(qf.selectFrom(examsheet)
                .leftJoin(examsheet.problemsheets, problemsheet).fetchJoin()
                .where(examsheet.id.eq(examsheetId))
                .fetchFirst());
    }
}
