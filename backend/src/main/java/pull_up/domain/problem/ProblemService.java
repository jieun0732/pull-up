package pull_up.domain.problem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pull_up.domain.problem.dto.Create;
import pull_up.domain.dao.ProblemRepository;
import pull_up.api.dto.MessageDto;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    @Transactional
    public MessageDto createProblem(Create.Request createProblemReq) {
        Problem problem = Create.Request.toEntity(createProblemReq);
        problemRepository.save(problem);
        return new MessageDto("Problem " + problem.getId() + " has been created successfully.");
    }

    @Transactional
    public MessageDto createProblem(String formatString) {
        List<Problem> problems = Create.FormatRequest.toEntity(formatString);
        problemRepository.saveAll(problems);
        return new MessageDto(problems.size() +" Problems have been created successfully.");
    }
}
