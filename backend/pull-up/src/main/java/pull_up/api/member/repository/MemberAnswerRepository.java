package pull_up.api.member.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.member.entity.Member;
import pull_up.api.member.entity.MemberAnswer;
import pull_up.api.problem.entity.Problem;

public interface MemberAnswerRepository extends JpaRepository<MemberAnswer, Long>, MemberAnswerRepositoryCustom {
    @Query("SELECT ma FROM MemberAnswer ma JOIN ma.problem p WHERE ma.member = :member AND ma.isCorrect = false AND p.category = :category AND p.entry = :entry AND p.type = :type")
    List<MemberAnswer> findIncorrectAnswers(@Param("member") Member member, @Param("category") String category, @Param("entry") String entry, @Param("type") String type);

    List<MemberAnswer> findByMemberIdAndProblemEntryAndProblemCategory(Long memberId, String entry, String category);

    List<MemberAnswer> findByMemberIdAndProblemEntryAndProblemCategoryAndProblemType(Long memberId, String entry, String category,
        String type);

    Optional<MemberAnswer> findByMemberAndProblem(Member member, Problem problem);

    boolean existsByMemberIdAndProblemId(Long memberId, Long id);

    @Query("SELECT COUNT(ma) FROM MemberAnswer ma WHERE ma.member.id = :memberId AND ma.problem.id = :problemId AND ma.chosenAnswer IS NOT NULL")
    Long countAnsweredProblemsByMemberAndProblem(@Param("memberId") Long memberId, @Param("problemId") Long problemId);

    boolean existsByMemberIdAndProblemIdAndIsCorrect(Long memberId, Long id, boolean b);
}
