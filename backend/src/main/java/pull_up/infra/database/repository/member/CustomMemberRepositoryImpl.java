package pull_up.infra.database.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pull_up.infra.database.entity.*;

import java.util.List;

import static pull_up.infra.database.entity.QExamInformation.examInformation;
import static pull_up.infra.database.entity.QExamProblem.examProblem;
import static pull_up.infra.database.entity.QIncorrectAnswer.incorrectAnswer;
import static pull_up.infra.database.entity.QMember.member;
import static pull_up.infra.database.entity.QMemberAnswer.memberAnswer;

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

        List<ExamProblem> examProblems = qf.selectFrom(examProblem)
                .leftJoin(examProblem.examInformation, examInformation).fetchJoin()
                .where(examInformation.member.id.eq(id))
                .fetch();

        List<MemberAnswer> memberAnswers = qf.selectFrom(memberAnswer)
                .leftJoin(memberAnswer.member, member)
                .where(member.id.eq(id))
                .fetch();

        List<IncorrectAnswer> incorrectAnswers = qf.selectFrom(incorrectAnswer)
                .leftJoin(incorrectAnswer.member, member)
                .where(member.id.eq(id))
                .fetch();

        ret.setExamInformationList(examProblems.stream().map(ExamProblem::getExamInformation).toList());
        ret.setMemberAnswers(memberAnswers);
        ret.setIncorrectAnswers(incorrectAnswers);
        return ret;
    }
}
