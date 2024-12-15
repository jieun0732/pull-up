package pull_up.infra.database.jpa.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.bouncycastle.oer.its.ieee1609dot2.Opaque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import pull_up.domain.exam.ExamType;
import pull_up.domain.exam.dto.ExamInfo;
import pull_up.domain.member.dto.MemberListInfo;
import pull_up.infra.database.jpa.dto.IncorrectQueryDto;
import pull_up.infra.database.jpa.dto.QIncorrectQueryDto;
import pull_up.infra.database.jpa.dto.SearchParam;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.entity.QMember;

import java.util.List;

import static pull_up.infra.database.jpa.entity.QAnswer.answer;
import static pull_up.infra.database.jpa.entity.QExam.exam;
import static pull_up.infra.database.jpa.entity.QMember.member;
import static pull_up.infra.database.jpa.entity.QProblem.problem;

@Repository
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory qf;

    public CustomMemberRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public List<IncorrectQueryDto> getIncorrectAnswersById(Long memberId) {
        return qf.select(new QIncorrectQueryDto(exam, problem, answer))
                .from(answer)
                .leftJoin(answer.problem, problem)
                .leftJoin(answer.exam, exam)
                .leftJoin(answer.exam.member, member).on(answer.exam.member.eq(member))
                .where(member.id.eq(memberId)
                        .and(answer.isCorrect.isFalse())
                        .and(exam.examType.eq(ExamType.EVENLY)
                                .or(exam.examType.eq(ExamType.BY_PROBLEM_TYPE))))
                .fetch();
    }

    @Override
    public Page<MemberListInfo> searchMember(SearchParam searchParam) {
        List<Member> members = qf.selectFrom(member)
                .offset(searchParam.pageable().getOffset())
                .limit(searchParam.pageable().getPageSize())
                .fetch();

        List<MemberListInfo> memberInfos = members.stream().map(MemberListInfo::toDto).toList();

        Long count = qf.select(member.count())
                .from(member)
                .fetchFirst();

        return new PageImpl<>(memberInfos, searchParam.pageable(), count);
    }
}
