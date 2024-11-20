package pull_up.infra.database.jpa.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pull_up.infra.database.jpa.entity.Member;

import java.util.Optional;

@Repository
public class CustomMemberRepositoryImpl implements CustomMemberRepository{

    private final JPAQueryFactory qf;

    public CustomMemberRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Member> findMemberSolvedInfo(Long id) {
        return null;
    }
}
