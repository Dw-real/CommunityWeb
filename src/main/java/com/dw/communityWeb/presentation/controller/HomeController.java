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
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("userId", loggedInUser.getId());
        } else {
            model.addAttribute("loggedIn", false);
            model.addAttribute("userId", "");
        }

        return "home";
    }
}
