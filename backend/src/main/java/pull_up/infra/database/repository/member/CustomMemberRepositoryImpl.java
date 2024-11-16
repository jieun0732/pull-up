package pull_up.infra.database.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pull_up.infra.database.entity.legacy.AnswerL;
import pull_up.infra.database.entity.legacy.IncorrectAnswer;
import pull_up.infra.database.entity.legacy.MemberL;
import pull_up.infra.database.entity.legacy.MemberAnswer;

import java.util.List;

import static pull_up.infra.database.entity.legacy.QAnswerL.answerL;
import static pull_up.infra.database.entity.legacy.QExamL.examL;
import static pull_up.infra.database.entity.legacy.QMemberL.memberL;
import static pull_up.infra.database.entity.legacy.QIncorrectAnswer.incorrectAnswer;
import static pull_up.infra.database.entity.legacy.QMemberAnswer.memberAnswer;

@Repository
public class CustomMemberRepositoryImpl implements CustomMemberRepository{

    private final JPAQueryFactory qf;

    public CustomMemberRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public MemberL findMemberByIdWithRelation(Long id) {
        MemberL ret = qf.selectFrom(memberL)
                .where(memberL.id.eq(id))
                .fetchFirst();

        List<AnswerL> answerLS = qf.selectFrom(answerL)
                .leftJoin(answerL.examL, examL).fetchJoin()
                .where(examL.memberL.id.eq(id))
                .fetch();

        List<MemberAnswer> memberAnswers = qf.selectFrom(memberAnswer)
                .leftJoin(memberAnswer.memberL, memberL)
                .where(memberL.id.eq(id))
                .fetch();

        List<IncorrectAnswer> incorrectAnswers = qf.selectFrom(incorrectAnswer)
                .leftJoin(incorrectAnswer.memberL, memberL)
                .where(memberL.id.eq(id))
                .fetch();

        ret.setExamLList(answerLS.stream().map(AnswerL::getExamL).toList());
        ret.setMemberAnswers(memberAnswers);
        ret.setIncorrectAnswers(incorrectAnswers);
        return ret;
    }
}
