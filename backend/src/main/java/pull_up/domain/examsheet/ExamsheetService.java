package pull_up.domain.examsheet;

import pull_up.domain.examsheet.dto.CreateExamsheet;
import pull_up.global.dto.MessageDto;

public class ExamsheetService {
    public MessageDto createExamsheet(CreateExamsheet.Request createExamsheetReq) {
        return new MessageDto("시험 문제가 정상 생성되었습니다.");
    }
}
