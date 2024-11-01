package pull_up.api.problem.repository;

import pull_up.api.problem.dto.ProblemDto;

import java.util.List;

public interface CustomProblemRepository {
    List<ProblemDto> findByEntryAndCategory(String entry, String category);
}
