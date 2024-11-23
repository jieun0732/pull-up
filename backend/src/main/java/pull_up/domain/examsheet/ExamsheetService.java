package pull_up.domain.examsheet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.examsheet.dto.CreateExamsheet;
import pull_up.api.dto.MessageDto;
import pull_up.infra.database.jpa.entity.Examsheet;

@Service
@RequiredArgsConstructor
public class ExamsheetService {

    private final ExamsheetRepository examsheetRepository;

    public MessageDto createExamsheet(CreateExamsheet.Request createExamsheetReq) {

        Examsheet examsheet = Examsheet.create(createExamsheetReq.examTitle(), createExamsheetReq.getProblemMap());
        examsheetRepository.save(examsheet);

        return new MessageDto("시험 문제 [id : " + examsheet.getId() + ", 제목 : " + examsheet.getExamTitle() + "] 가 정상 생성되었습니다.");
    }
}
