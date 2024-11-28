package pull_up.domain.problem;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pull_up.domain.problem.dto.Create;
import pull_up.domain.dao.ProblemRepository;
import pull_up.api.dto.MessageDto;
import pull_up.domain.problem.dto.ProblemDetailInfo;
import pull_up.infra.database.jpa.dto.ProblemInfo;
import pull_up.domain.problem.exception.ProblemErrorCode;
import pull_up.domain.problem.exception.ProblemException;
import pull_up.infra.database.jpa.dto.SearchParam;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public Page<ProblemInfo> getAll(SearchParam searchParam) {
        return problemRepository.searchProblem(searchParam);
    }

    public ProblemDetailInfo get(Long problemId) {
        return ProblemDetailInfo.toDto(problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM)));
    }

    @Transactional
    public MessageDto create(Create.Request createProblemReq) {
        Problem problem = Create.Request.toEntity(createProblemReq);
        problemRepository.save(problem);
        return new MessageDto("Problem " + problem.getId() + " has been created successfully.");
    }

    @Transactional
    public MessageDto create(String formatString) {
        List<Problem> problems = Create.FormatRequest.toEntity(formatString);
        problemRepository.saveAll(problems);
        return new MessageDto(problems.size() +" Problems have been created successfully.");
    }

    @Transactional
    public ProblemDetailInfo modify(Long problemId, Map<String, String> parameters) {
        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM));
        problem.modify(parameters);
        return ProblemDetailInfo.toDto(problem);
    }

    @Transactional
    public ProblemDetailInfo create(Map<String, String> parameters) {
        Problem problem = Problem.create(parameters);
        problemRepository.save(problem);
        return ProblemDetailInfo.toDto(problem);
    }

    @Transactional
    public void delete(Long problemId) {
        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM));
        problemRepository.delete(problem);
    }
}
