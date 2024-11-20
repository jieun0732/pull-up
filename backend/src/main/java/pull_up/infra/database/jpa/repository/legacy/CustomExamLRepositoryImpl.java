package pull_up.infra.database.jpa.repository.legacy;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pull_up.infra.database.jpa.entity.legacy.ExamL;

import java.util.Optional;

import static pull_up.infra.database.jpa.entity.legacy.QAnswerL.answerL;
import static pull_up.infra.database.jpa.entity.legacy.QExamL.examL;
public class CustomExamLRepositoryImpl implements CustomExamLRepository {

    private final JPAQueryFactory qf;

    public CustomExamLRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public Optional<ExamL> findByIdWithAnswer(Long id) {
        return Optional.ofNullable(qf.selectFrom(examL)
                .leftJoin(examL.answerLS, answerL).fetchJoin()
                .fetchFirst());
    }
}
