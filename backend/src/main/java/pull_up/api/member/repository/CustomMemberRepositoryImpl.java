package pull_up.api.member.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.exam.entity.ExamProblem;
import pull_up.api.exam.entity.QExamInformation;
import pull_up.api.exam.entity.QExamProblem;
import pull_up.api.member.entity.IncorrectAnswer;
import pull_up.api.member.entity.Member;
import pull_up.api.member.entity.MemberAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pull_up.api.exam.entity.QExamInformation.examInformation;
import static pull_up.api.exam.entity.QExamProblem.examProblem;
import static pull_up.api.member.entity.QIncorrectAnswer.incorrectAnswer;
import static pull_up.api.member.entity.QMember.member;
import static pull_up.api.member.entity.QMemberAnswer.memberAnswer;

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

        ret.setExamInformations(examProblems.stream().map(ExamProblem::getExamInformation).toList());
        ret.setMemberAnswers(memberAnswers);
        ret.setIncorrectAnswers(incorrectAnswers);
        return ret;
    }
}
