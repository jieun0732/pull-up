package pull_up.infra.database.jpa.repository.exam;

import org.springframework.data.domain.Page;
import pull_up.domain.exam.dto.ExamInfo;
import pull_up.domain.member.dto.SolvedInfo;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.dto.SearchParam;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomExamRepository {
    Map<String, Exam> findAllEvenlyAndProblemTypeExamMap(Long memberId, Entry entry);

    Optional<Exam> findEvenlyExamByMemberIdAndExamTitle(Long memberId, String examTitle);

    Optional<Exam> findMockExamByMemberId(Long memberId);

    SolvedInfo.MockExamResponse findSolvedMockExamInfo(Long memberId);

    List<Exam> findAllByMemberId(Long memberId);

    Page<ExamInfo> searchExam(SearchParam searchParam);
}
