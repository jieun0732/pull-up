package pull_up.infra.database.repository.exam;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.entity.QAnsweredProblem;
import pull_up.infra.database.entity.QExam;

import java.util.Optional;

import static pull_up.infra.database.entity.QAnsweredProblem.answeredProblem;
import static pull_up.infra.database.entity.QExam.exam;

public class CustomExamRepositoryImpl implements CustomExamRepository {

    private final JPAQueryFactory qf;

    public CustomExamRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Exam> findByIdWithAnswer(Long id) {
        return Optional.ofNullable(qf.selectFrom(exam)
                .leftJoin(exam.answeredProblems, answeredProblem).fetchJoin()
                .fetchFirst());
    }
}
