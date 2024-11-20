package pull_up.domain.deprecated;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.infra.database.jpa.entity.legacy.MemberAnswer;
import pull_up.infra.database.jpa.entity.legacy.MemberL;
import pull_up.infra.database.jpa.entity.legacy.ProblemL;
import pull_up.infra.database.jpa.repository.legacy.MemberAnswerRepository;
import pull_up.infra.database.jpa.repository.member.MemberRepositoryL;
import pull_up.infra.database.jpa.repository.legacy.ProblemLRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberAnswerService {

    private final MemberAnswerRepository memberAnswerRepository;

    private final ProblemLRepository problemLRepository;

    private final MemberRepositoryL memberRepositoryL;

    public void createMemberAnswersForNonMockExamProblems(Long memberId) {
        // Member 조회
        MemberL memberL = memberRepositoryL.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        // category가 "모의고사"가 아닌 모든 문제 조회
        List<ProblemL> problemLS = problemLRepository.findByCategoryNot("모의고사");

        // MemberAnswer 생성 및 저장
        for (ProblemL problemL : problemLS) {
            MemberAnswer memberAnswer = MemberAnswer.of(memberL, problemL, null, null, false);
            memberAnswerRepository.save(memberAnswer);
        }
    }
}
