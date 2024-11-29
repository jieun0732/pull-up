package pull_up.api.view;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.URIEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import pull_up.domain.problem.Entry;
import pull_up.domain.problem.ProblemService;
import pull_up.domain.problem.dto.ProblemDetailInfo;
import pull_up.infra.database.jpa.dto.ProblemInfo;
import pull_up.infra.database.jpa.dto.SearchParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static pull_up.api.util.BaseUrlParser.getBaseUrlWithQueryString;

@Controller
@RequestMapping("/admin/problems")
@RequiredArgsConstructor
public class AdminProblemController {

    private final ProblemService problemService;

    @GetMapping
    public String problemListPage(Model model,
                                  @RequestParam(required = false) String searchType,
                                  @RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) String sortOrder,
                                  @RequestParam(required = false) String sortType,
                                  HttpServletRequest request,
                                  Pageable pageable) {
        Page<ProblemInfo> problemInfos = problemService.getAll(SearchParam.getSearchParam(searchType, keyword, sortOrder, sortType, pageable));
        model.addAttribute("problemInfos", problemInfos);
        model.addAttribute("navBtnColor", "problems");
        model.addAttribute("baseURI", getBaseUrlWithQueryString(request));
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("pagination", IntStream.rangeClosed(1, problemInfos.getTotalPages()).boxed().toList());
        return "problems/list";
    }

    @GetMapping("/{problemId}")
    public String problemListDetail(Model model, @PathVariable Long problemId) {
        ProblemDetailInfo problemInfo = problemService.get(problemId);
        model.addAttribute("problemInfo", problemInfo);
        model.addAttribute("entry", Entry.values());
        model.addAttribute("navBtnColor", "problems");
        return "problems/detail";
    }

    @GetMapping("/new")
    public String problemCreatePage(Model model) {
        model.addAttribute("entry", Entry.values());
        model.addAttribute("choices", List.of(1, 2, 3, 4, 5));
        model.addAttribute("navBtnColor", "problems");
        return "problems/create";
    }

    @PostMapping
    public String create(Model model, @RequestParam Map<String, String> parameters) {
        ProblemDetailInfo problemInfo = problemService.create(parameters);
        model.addAttribute("problemInfo", problemInfo);
        model.addAttribute("entry", Entry.values());
        model.addAttribute("navBtnColor", "problems");
        return "problems/detail";
    }

    @PatchMapping("/{problemId}")
    public String modify(Model model, @PathVariable Long problemId, @RequestParam Map<String, String> parameters) {
        ProblemDetailInfo problemInfo = problemService.modify(problemId, parameters);
        model.addAttribute("problemInfo", problemInfo);
        model.addAttribute("entry", Entry.values());
        model.addAttribute("navBtnColor", "problems");
        return "problems/detail";
    }

    @DeleteMapping("/{problemId}")
    public void delete(HttpServletResponse response, @PathVariable Long problemId) throws IOException {
        problemService.delete(problemId);
        response.sendRedirect("/admin/lists");
    }
}
