package pull_up.api.view;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pull_up.domain.problem.Entry;
import pull_up.domain.problem.ProblemService;
import pull_up.domain.problem.dto.ProblemDetailInfo;
import pull_up.domain.problem.dto.ProblemInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/lists")
@RequiredArgsConstructor
public class AdminProblemController {

    private final ProblemService problemService;

    @GetMapping
    public String problemListPage(Model model) {
        List<ProblemInfo> problemInfos = problemService.getAll();
        model.addAttribute("problemInfos", problemInfos);
        model.addAttribute("navBtnColor","list");
        return "problems/list";
    }

    @GetMapping("/{problemId}")
    public String problemListDetail(Model model, @PathVariable Long problemId) {
        ProblemDetailInfo problemInfo = problemService.get(problemId);
        model.addAttribute("problemInfo", problemInfo);
        model.addAttribute("entry", Entry.values());
        model.addAttribute("navBtnColor","list");
        return "problems/detail";
    }

    @GetMapping("/new")
    public String problemCreatePage(Model model) {
        model.addAttribute("entry", Entry.values());
        model.addAttribute("choices", List.of(1, 2, 3, 4, 5));
        model.addAttribute("navBtnColor","list");
        return "problems/create";
    }

    @PostMapping
    public String create(Model model, @RequestParam Map<String, String> parameters) {
        ProblemDetailInfo problemInfo = problemService.create(parameters);
        model.addAttribute("problemInfo", problemInfo);
        model.addAttribute("entry", Entry.values());
        model.addAttribute("navBtnColor","list");
        return "problems/detail";
    }

    @PatchMapping("/{problemId}")
    public String modify(Model model, @PathVariable Long problemId, @RequestParam Map<String, String> parameters) {
        ProblemDetailInfo problemInfo = problemService.modify(problemId, parameters);
        model.addAttribute("problemInfo", problemInfo);
        model.addAttribute("entry", Entry.values());
        model.addAttribute("navBtnColor","list");
        return "problems/detail";
    }

    @DeleteMapping("/{problemId}")
    public void delete(HttpServletResponse response, @PathVariable Long problemId) throws IOException {
        problemService.delete(problemId);
        response.sendRedirect("/admin/lists");
    }
}
