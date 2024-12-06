package pull_up.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.dto.ExamInfo;
import pull_up.domain.problem.dto.ProblemInfo;
import pull_up.infra.database.jpa.dto.SearchParam;

import java.io.IOException;
import java.util.stream.IntStream;

import static pull_up.api.util.BaseUrlParser.getBaseUrlWithQueryString;

@Controller
@RequestMapping("/admin/exams")
@RequiredArgsConstructor
public class AdminExamController {

    private final ExamService examService;

    @GetMapping
    public String examListPage(Model model,
                                  @RequestParam(required = false) String searchType,
                                  @RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) String sortOrder,
                                  @RequestParam(required = false) String sortType,
                                  HttpServletRequest request,
                                  Pageable pageable) {
        Page<ExamInfo> examInfos = examService.getAll(SearchParam.getSearchParam(searchType, keyword, sortOrder, sortType, pageable));
        model.addAttribute("examInfos", examInfos);
        model.addAttribute("baseURI", getBaseUrlWithQueryString(request));
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("pagination", IntStream.rangeClosed(1, examInfos.getTotalPages()).boxed().toList());
        return "exams/list";
    }

    @DeleteMapping("/{examId}")
    public void delete(@PathVariable Long examId, HttpServletResponse response) throws IOException {
        examService.reset(examId);
        response.sendRedirect("/admin/exams");
    }
}
