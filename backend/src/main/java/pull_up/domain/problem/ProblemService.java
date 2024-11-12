package pull_up.domain.problem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.problem.dto.CreateProblem;
import pull_up.global.dto.MessageDto;
import pull_up.infra.database.entity.Problem;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public void deleteAllProblem() {
        problemRepository.deleteAll();
    }

    public MessageDto createProblem(CreateProblem.Request createProblemReq) {
        Problem problem = CreateProblem.Request.toEntity(createProblemReq);
        problemRepository.save(problem);
        return new MessageDto("Problem " + problem.getId() + " has been created successfully.");
    }
}
