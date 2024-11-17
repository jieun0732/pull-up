package pull_up.domain.problem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pull_up.api.problem.dto.CreateProblem;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.global.dto.ListDto;
import pull_up.global.dto.MessageDto;
import pull_up.infra.database.entity.legacy.ProblemL;
import pull_up.infra.database.repository.problem.ProblemRepositoryL;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepositoryL problemRepository;

    @Transactional
    public void deleteAllProblem() {
        problemRepository.deleteAllWithRelation();
    }

    @Transactional
    public MessageDto createProblem(CreateProblem.Request createProblemReq) {
        ProblemL problemL = CreateProblem.Request.toEntity(createProblemReq);
        problemRepository.save(problemL);
        return new MessageDto("Problem " + problemL.getId() + " has been created successfully.");
    }

    @Transactional
    public MessageDto createProblem(String formatString) {
        List<ProblemL> problemLS = CreateProblem.FormatRequest.toEntity(formatString);
        problemRepository.saveAll(problemLS);
        return new MessageDto(problemLS.size() +" Problems have been created successfully.");
    }

    public ListDto<ProblemDto> getList(String entry, String category, String type) {
        List<ProblemL> problemLS = problemRepository.findByEntryAndCategoryAndType(entry, category, type);
        return new ListDto<>(problemLS.stream().map(ProblemDto::toDto).toList());
    }
}
