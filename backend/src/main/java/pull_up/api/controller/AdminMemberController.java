package pull_up.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pull_up.domain.member.MemberService;
import pull_up.domain.member.dto.MemberListInfo;
import pull_up.infra.database.jpa.dto.SearchParam;

import java.util.stream.IntStream;

import static pull_up.api.util.BaseUrlParser.getBaseUrlWithQueryString;

@Controller
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping
    public String examListPage(Model model,
                               @RequestParam(required = false) String searchType,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String sortOrder,
                               @RequestParam(required = false) String sortType,
                               HttpServletRequest request,
                               Pageable pageable) {
        Page<MemberListInfo> memberInfos = memberService.getAll(SearchParam.getSearchParam(searchType, keyword, sortOrder, sortType, pageable));
        model.addAttribute("memberInfos", memberInfos);
        model.addAttribute("baseURI", getBaseUrlWithQueryString(request));
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("pagination", IntStream.rangeClosed(1, memberInfos.getTotalPages()).boxed().toList());
        return "members/list";
    }
}
