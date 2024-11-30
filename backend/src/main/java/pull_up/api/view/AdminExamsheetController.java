package pull_up.api.view;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pull_up.domain.examsheet.ExamsheetService;
import pull_up.infra.database.jpa.dto.ExamsheetInfo;
import pull_up.infra.database.jpa.dto.ProblemInfo;
import pull_up.infra.database.jpa.dto.SearchParam;

import java.util.List;
import java.util.stream.IntStream;

import static pull_up.api.util.BaseUrlParser.getBaseUrlWithQueryString;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/examsheets")
public class AdminExamsheetController {

    private final ExamsheetService examsheetService;

    @GetMapping
    public String examsheetListPage(Model model) {
        List<ExamsheetInfo> examsheetInfos = examsheetService.getAll();
        model.addAttribute("examsheetInfos", examsheetInfos);
        model.addAttribute("navBtnColor", "list");
        return "examsheets/list";
    }
}
