package com.dw.communityWeb.presentation.controller;

import com.dw.communityWeb.application.BoardService;
import com.dw.communityWeb.presentation.dto.board.BoardDto;
import com.dw.communityWeb.presentation.dto.user.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final BoardService boardService;

    @Autowired
    public HomeController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String home(HttpSession session, @PageableDefault(page = 1) Pageable pageable, Model model) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("userId", loggedInUser.getId());
        } else {
            model.addAttribute("loggedIn", false);
            model.addAttribute("userId", "");
        }

        Page<BoardDto> boardList = boardService.paging(pageable);

        int blockLimit = 10;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = (boardList.getTotalPages() == 0) ? 1 : Math.min((startPage + blockLimit - 1), boardList.getTotalPages());

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "home";
    }
}
