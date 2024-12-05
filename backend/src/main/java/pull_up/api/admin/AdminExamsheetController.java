package pull_up.api.admin;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pull_up.domain.examsheet.ExamsheetService;
import pull_up.domain.examsheet.dto.ExamsheetDetailInfo;
import pull_up.infra.database.jpa.dto.ExamsheetInfo;
import pull_up.infra.database.jpa.dto.ProblemInfo;
import pull_up.infra.database.jpa.dto.SearchParam;

import java.util.List;
import java.util.Map;
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
        return "examsheets/list";
    }

    @GetMapping("/{examsheetId}")
    public String examsheetDetail(@PathVariable Long examsheetId, Model model) {
        ExamsheetDetailInfo examsheetInfo = examsheetService.get(examsheetId);
        model.addAttribute("examsheetInfo", examsheetInfo);
        return "examsheets/detail";
    }

    @PatchMapping("/{examsheetId}")
    public String examsheetUpdate(@PathVariable Long examsheetId, @RequestParam Map<String, String> parameters, Model model) {
        ExamsheetDetailInfo examsheetInfo = examsheetService.update(examsheetId, parameters);
        model.addAttribute("examsheetInfo", examsheetInfo);
        return "examsheets/detail";
    }

    @GetMapping("/{examsheetId}/select-problem/{problemNumber}")
    public String problemListPage(Model model,
                                  @PathVariable Long examsheetId,
                                  @PathVariable Integer problemNumber,
                                  @RequestParam Long selectedId,
                                  @RequestParam(required = false) String searchType,
                                  @RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) String sortOrder,
                                  @RequestParam(required = false) String sortType,
                                  HttpServletRequest request,
                                  Pageable pageable) {
        Page<ProblemInfo> problemInfos = examsheetService.getAllProblems(SearchParam.getSearchParam(searchType, keyword, sortOrder, sortType, pageable));
        model.addAttribute("selectedId",selectedId);
        model.addAttribute("problemInfos", problemInfos);
        model.addAttribute("baseURI", getBaseUrlWithQueryString(request));
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("pagination", IntStream.rangeClosed(1, problemInfos.getTotalPages()).boxed().toList());
        return "examsheets/change-problem";
    }

    @PatchMapping("/{examsheetId}/select-problem/{problemNumber}")
    public String changeProblem(Model model,
                                  @PathVariable Long examsheetId,
                                  @PathVariable Integer problemNumber,
                                  @RequestParam Long newSelectedId) {
        ExamsheetDetailInfo examsheetInfo = examsheetService.changeProblem(examsheetId, problemNumber, newSelectedId);
        model.addAttribute("examsheetInfo", examsheetInfo);
        return "examsheets/detail";
    }
}
