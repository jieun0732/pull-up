package pull_up.api.exam.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.exam.entity.ExamProblem;
import pull_up.api.exam.repository.ExamInformationRepository;
import pull_up.api.exam.repository.ExamProblemRepository;
import pull_up.api.member.entity.Member;
import pull_up.api.member.exception.MemberErrorCode;
import pull_up.api.member.exception.MemberException;
import pull_up.api.member.repository.MemberRepository;
import pull_up.api.problem.entity.Problem;
import pull_up.api.problem.repository.ProblemRepository;

@Service
public class ExamService {

    @Autowired
    private ExamInformationRepository examInformationRepository;

    @Autowired
    private ExamProblemRepository examProblemRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private MemberRepository memberRepository;

    public ExamInformationDto createMockExam(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        // Passing the necessary parameters
        LocalDateTime now = LocalDateTime.now();
        ExamInformation examInformation = ExamInformation.of(member, "모의고사", now, null, null);
        examInformation = examInformationRepository.save(examInformation);

        List<Problem> problems = problemRepository.findByCategory("모의고사");
        for (Problem problem : problems) {
            ExamProblem examProblem = ExamProblem.of(examInformation, problem);
            examProblemRepository.save(examProblem);
        }

        return ExamInformationDto.from(examInformation);
    }
}
