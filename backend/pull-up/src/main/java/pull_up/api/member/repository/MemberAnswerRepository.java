package pull_up.api.member.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.member.entity.Member;
import pull_up.api.member.entity.MemberAnswer;

public interface MemberAnswerRepository extends JpaRepository<MemberAnswer, Long> {
    @Query("SELECT ma FROM MemberAnswer ma JOIN ma.problem p WHERE ma.member = :member AND ma.isCorrect = false AND p.category = :category AND p.entry = :entry AND p.type = :type")
    List<MemberAnswer> findIncorrectAnswers(@Param("member") Member member, @Param("category") String category, @Param("entry") String entry, @Param("type") String type);
}
