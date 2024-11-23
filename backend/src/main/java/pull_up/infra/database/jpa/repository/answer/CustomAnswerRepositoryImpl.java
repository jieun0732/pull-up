package pull_up.infra.database.jpa.repository.answer;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class CustomAnswerRepositoryImpl implements CustomAnswerRepository {

    private final JPAQueryFactory qf;

    public CustomAnswerRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

}
