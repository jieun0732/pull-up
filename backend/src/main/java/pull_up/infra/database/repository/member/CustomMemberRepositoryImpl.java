package pull_up.infra.database.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pull_up.infra.database.entity.*;
import pull_up.infra.database.entity.legacy.IncorrectAnswer;
import pull_up.infra.database.entity.legacy.MemberAnswer;

import java.util.List;

import static pull_up.infra.database.entity.QAnswer.answer;
import static pull_up.infra.database.entity.QExam.exam;
import static pull_up.infra.database.entity.QMember.member;
import static pull_up.infra.database.entity.legacy.QIncorrectAnswer.incorrectAnswer;
import static pull_up.infra.database.entity.legacy.QMemberAnswer.memberAnswer;

@Repository
public class CustomMemberRepositoryImpl implements CustomMemberRepository{

    private final JPAQueryFactory qf;

    public CustomMemberRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public Member findMemberByIdWithRelation(Long id) {
        Member ret = qf.selectFrom(member)
                .where(member.id.eq(id))
                .fetchFirst();

        List<Answer> answers = qf.selectFrom(answer)
                .leftJoin(answer.exam, exam).fetchJoin()
                .where(exam.member.id.eq(id))
                .fetch();

        List<MemberAnswer> memberAnswers = qf.selectFrom(memberAnswer)
                .leftJoin(memberAnswer.member, member)
                .where(member.id.eq(id))
                .fetch();

        List<IncorrectAnswer> incorrectAnswers = qf.selectFrom(incorrectAnswer)
                .leftJoin(incorrectAnswer.member, member)
                .where(member.id.eq(id))
                .fetch();

        ret.setExamList(answers.stream().map(Answer::getExam).toList());
        ret.setMemberAnswers(memberAnswers);
        ret.setIncorrectAnswers(incorrectAnswers);
        return ret;
    }
}
