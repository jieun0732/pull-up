package pull_up.domain.problem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pull_up.api.problem.dto.CreateProblem;
import pull_up.global.dto.MessageDto;
import pull_up.infra.database.entity.Problem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    @Transactional
    public void deleteAllProblem() {
        problemRepository.deleteAllWithRelation();
    }

    @Transactional
    public MessageDto createProblem(CreateProblem.Request createProblemReq) {
        Problem problem = CreateProblem.Request.toEntity(createProblemReq);
        problemRepository.save(problem);
        return new MessageDto("Problem " + problem.getId() + " has been created successfully.");
    }

    @Transactional
    public MessageDto createProblem(String formatString) {
        List<Problem> problems = CreateProblem.FormatRequest.toEntity(formatString);
        problemRepository.saveAll(problems);
        return new MessageDto(problems.size() +" Problems have been created successfully.");
    }
}
