package pull_up.api.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pull_up.domain.examsheet.ExamsheetService;
import pull_up.domain.examsheet.dto.ExamsheetDetailInfo;
import pull_up.infra.database.jpa.dto.ExamsheetInfo;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/examsheets")
public class AdminExamsheetController {

    private final ExamsheetService examsheetService;

    @GetMapping
    public String examsheetListPage(Model model) {
        List<ExamsheetInfo> examsheetInfos = examsheetService.getAll();
        model.addAttribute("examsheetInfos", examsheetInfos);
        return "examsheets/list";
    }

    @GetMapping("/{examsheetId}")
    public String examsheetDetail(@PathVariable Long examsheetId, Model model) {
        ExamsheetDetailInfo examsheetInfo = examsheetService.get(examsheetId);
        model.addAttribute("examsheetInfo", examsheetInfo);
        return "examsheets/detail";
    }

}
