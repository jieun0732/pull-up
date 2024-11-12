package pull_up.domain.problem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public void deleteAllProblem() {
        problemRepository.deleteAll();
    }
}
