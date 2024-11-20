package pull_up.domain.deprecated;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.domain.dao.ProblemRepository;
import pull_up.global.dto.ListDto;
import pull_up.infra.database.jpa.entity.legacy.ProblemL;
import pull_up.infra.database.jpa.repository.legacy.ProblemLRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemServiceL {

    private final ProblemLRepository problemRepository;

    @Deprecated
    public ListDto<ProblemDto> getList(String entry, String category, String type) {
        List<ProblemL> problemLS = problemRepository.findByEntryAndCategoryAndType(entry, category, type);
        return new ListDto<>(problemLS.stream().map(ProblemDto::toDto).toList());
    }
}
