package pull_up.infra.database.repository.exam;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pull_up.infra.database.entity.legacy.ExamL;

import java.util.Optional;

import static pull_up.infra.database.entity.legacy.QAnswerL.answerL;
import static pull_up.infra.database.entity.legacy.QExamL.examL;
public class CustomExamRepositoryImpl implements CustomExamRepository {

    private final JPAQueryFactory qf;

    public CustomExamRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public Optional<ExamL> findByIdWithAnswer(Long id) {
        return Optional.ofNullable(qf.selectFrom(examL)
                .leftJoin(examL.answerLS, answerL).fetchJoin()
                .fetchFirst());
    }
}
