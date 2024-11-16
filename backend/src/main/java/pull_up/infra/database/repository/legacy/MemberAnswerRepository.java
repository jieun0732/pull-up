package pull_up.infra.database.repository.legacy;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pull_up.infra.database.entity.legacy.MemberL;
import pull_up.infra.database.entity.legacy.MemberAnswer;
import pull_up.infra.database.entity.legacy.ProblemL;

public interface MemberAnswerRepository extends JpaRepository<MemberAnswer, Long>, CustomMemberAnswerRepository {
    Optional<MemberAnswer> findById(Long MemberAnswerid);
    @Query("SELECT ma FROM MemberAnswer ma JOIN ma.problemL p WHERE ma.memberL = :member AND ma.isCorrect = false AND p.category = :category AND p.entry = :entry AND p.type = :type")
    List<MemberAnswer> findIncorrectAnswers(@Param("member") MemberL memberL, @Param("category") String category, @Param("entry") String entry, @Param("type") String type);

    List<MemberAnswer> findByMemberLIdAndProblemLEntryAndProblemLCategory(Long memberId, String entry, String category);

    List<MemberAnswer> findByMemberLIdAndProblemLEntryAndProblemLCategoryAndProblemLType(Long memberId, String entry, String category,
                                                                                         String type);

    Optional<MemberAnswer> findByMemberLAndProblemL(MemberL memberL, ProblemL problemL);

    @Query("SELECT COUNT(ma) FROM MemberAnswer ma WHERE ma.memberL.id = :memberId AND ma.problemL.id = :problemId AND ma.chosenAnswer IS NOT NULL")
    Long countAnsweredProblemsByMemberAndProblem(@Param("memberId") Long memberId, @Param("problemId") Long problemId);

    boolean existsByMemberLIdAndProblemLIdAndIsCorrect(Long memberId, Long id, boolean b);
}
