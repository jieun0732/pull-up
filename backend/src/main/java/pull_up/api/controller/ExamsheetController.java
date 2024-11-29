package pull_up.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pull_up.domain.examsheet.ExamsheetService;
import pull_up.domain.examsheet.dto.CreateExamsheet;
import pull_up.api.dto.MessageDto;

@RestController
@RequestMapping("/api/examsheets")
@RequiredArgsConstructor
public class ExamsheetController {

    private final ExamsheetService examsheetService;

    @Operation(summary = "시험지 생성", description = "모의고사 시험지를 생성합니다.", tags = "모의고사")
    @PostMapping
    public ResponseEntity<MessageDto> getSolvedInfo(@RequestBody CreateExamsheet.Request request) {
        return new ResponseEntity<>(examsheetService.createExamsheet(request), HttpStatus.OK);
    }
}
