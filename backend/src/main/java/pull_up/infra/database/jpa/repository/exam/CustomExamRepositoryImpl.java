package pull_up.infra.database.jpa.repository.exam;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import pull_up.domain.exam.ExamType;
import pull_up.domain.member.dto.SolvedInfo;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.entity.QExamsheet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pull_up.infra.database.jpa.entity.QAnswer.answer;
import static pull_up.infra.database.jpa.entity.QExam.exam;
import static pull_up.infra.database.jpa.entity.QExamsheet.examsheet;
import static pull_up.infra.database.jpa.entity.QMember.member;
import static pull_up.infra.database.jpa.entity.QProblem.problem;

public class CustomExamRepositoryImpl implements CustomExamRepository {

    private final JPAQueryFactory qf;

    public CustomExamRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }


    @Override
    public Map<String, Exam> findAllEvenlyAndProblemTypeExamMap(Long memberId, Entry entry) {
        List<Exam> byProblemTypeExam = qf.selectFrom(exam)
                .leftJoin(exam.member, member)
                .leftJoin(exam.answers, answer).fetchJoin()
                .leftJoin(answer.problem, problem).fetchJoin()
                .where(exam.member.id.eq(memberId)
                        .and(exam.examType.eq(ExamType.BY_PROBLEM_TYPE))
                        .and(answer.problem.entry.eq(entry)))
                .fetch();

        Exam evenlyExam = qf.selectFrom(exam)
                .leftJoin(exam.member, member)
                .leftJoin(exam.answers, answer).fetchJoin()
                .leftJoin(answer.problem, problem).fetchJoin()
                .where(exam.member.id.eq(memberId)
                        .and(exam.examType.eq(ExamType.EVENLY)))
                .fetchFirst();

        Map<String, Exam> examMap = new HashMap<>();

        if (evenlyExam != null) examMap.put(ExamType.EVENLY.name(), evenlyExam);
        for (Exam exam : byProblemTypeExam) {
            examMap.put(exam.getAnswers().get(0).getProblem().getProblemType(), exam);
        }

        return examMap;
    }

    @Override
    public Optional<Exam> findEvenlyExamByMemberIdAndExamTitle(Long memberId, String examTitle) {
        return Optional.ofNullable(
                qf.selectFrom(exam)
                        .leftJoin(exam.examsheet, examsheet)
                        .where(exam.member.id.eq(memberId)
                                .and(exam.examType.eq(ExamType.MOCK_EXAM))
                                .and(examsheet.examTitle.equalsIgnoreCase(examTitle)))
                        .fetchFirst());
    }

    @Override
    public Optional<Exam> findMockExamByMemberId(Long memberId) {
        return Optional.ofNullable(
                qf.selectFrom(exam)
                        .where(exam.member.id.eq(memberId)
                                .and(exam.examType.eq(ExamType.MOCK_EXAM)))
                        .fetchFirst());
    }

    @Override
    public SolvedInfo.MockExamResponse findSolvedMockExamInfo(Long memberId) {
        return qf.select(Projections.constructor(SolvedInfo.MockExamResponse.class,
                        exam.id,
                        exam.isFinished,
                        member.tutorialFinished))
                .from(exam)
                .leftJoin(exam.member,member)
                .where(member.id.eq(memberId)
                        .and(exam.examType.eq(ExamType.MOCK_EXAM)))
                .fetchFirst();
    }

    @Override
    public List<Exam> findAllByMemberId(Long memberId) {
        return qf.selectFrom(exam)
                .where(exam.member.id.eq(memberId))
                .fetch();
    }
}
