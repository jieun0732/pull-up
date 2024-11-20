package pull_up.infra.database.jpa.repository.exam;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pull_up.domain.exam.ExamType;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.*;

import java.util.ArrayList;
import java.util.List;

import static pull_up.infra.database.jpa.entity.QAnswer.answer;
import static pull_up.infra.database.jpa.entity.QExam.exam;
import static pull_up.infra.database.jpa.entity.QMember.member;
import static pull_up.infra.database.jpa.entity.QProblem.problem;

public class CustomExamRepositoryImpl implements CustomExamRepository {

    private final JPAQueryFactory qf;

    public CustomExamRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }


    @Override
    public List<Exam> findAllByMemberIdAndEntry(Long memberId, Entry entry) {
        List<Exam> exams = qf.selectFrom(exam)
                .leftJoin(exam.member, member)
                .leftJoin(exam.answers, answer).fetchJoin()
                .leftJoin(answer.problem, problem).fetchJoin()
                .where(exam.member.id.eq(memberId)
                        .and(answer.problem.entry.eq(entry)))
                .fetch();

        Exam evenlyExam = qf.selectFrom(exam)
                .leftJoin(exam.member, member)
                .leftJoin(exam.answers, answer).fetchJoin()
                .leftJoin(answer.problem, problem).fetchJoin()
                .where(exam.member.id.eq(memberId)
                        .and(exam.examType.eq(ExamType.EVENLY)))
                .fetchFirst();

        exams.add(evenlyExam);
        return exams;
    }
}
