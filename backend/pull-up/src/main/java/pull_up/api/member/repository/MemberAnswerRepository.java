package pull_up.api.member.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.member.entity.MemberAnswer;

public interface MemberAnswerRepository extends JpaRepository<MemberAnswer, Long> {

    List<MemberAnswer> findByExamInformation(ExamInformation examInformation);
}
