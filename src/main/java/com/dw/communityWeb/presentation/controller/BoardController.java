package com.dw.communityWeb.presentation.controller;

import com.dw.communityWeb.application.BoardService;
import com.dw.communityWeb.application.CommentService;
import com.dw.communityWeb.presentation.dto.board.BoardUpdateDto;
import com.dw.communityWeb.presentation.dto.board.BoardDto;
import com.dw.communityWeb.presentation.dto.comment.CommentDto;
import com.dw.communityWeb.presentation.dto.user.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/community")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @Autowired
    public BoardController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }

    @GetMapping("/board/{type}")
    public String getBoardList(
            HttpSession session,
            @PathVariable String type,
            @PageableDefault(page = 1) Pageable pageable,
            Model model
    ) {
        Page<BoardDto> boardList = boardService.paging(type, pageable);
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("userId", loggedInUser.getId());
        } else {
            model.addAttribute("loggedIn", false);
            model.addAttribute("userId", "");
        }

        int blockLimit = 10;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = (boardList.getTotalPages() == 0) ? 1 : Math.min((startPage + blockLimit - 1), boardList.getTotalPages());
        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boardType", type);

        return "list";
    }

    @GetMapping("/viewMyPosts")
    public String viewMyPosts(HttpSession session, @PageableDefault(page = 1) Pageable pageable, Model model) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Page<BoardDto> boardList = boardService.getMyPosts(loggedInUser.getUserCode(), pageable);
            int blockLimit = 10;
            int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = (boardList.getTotalPages() == 0) ? 1 : Math.min((startPage + blockLimit - 1), boardList.getTotalPages());
            model.addAttribute("boardList", boardList);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }

        return "viewMyPosts";
    }

    @GetMapping("/post")
    public String postForm(HttpSession session, Model model) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser != null)
            model.addAttribute("user", loggedInUser);

        return "post";
    }

    @PostMapping("/post")
    public String post(@ModelAttribute BoardDto boardDto) throws IOException {
        boardService.post(boardDto);
        return "redirect:/";
    }

    @GetMapping("/{type}/{id}")
    public String findById(@PathVariable Long id,
                           @PathVariable String type, Model model,
                           @PageableDefault(page = 1) Pageable pageable,
                           HttpSession session) {
        // 로그인된 유저 확인
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        boolean isWriter = false;
        // 조회수 올리기
        boardService.updateHits(id);
        BoardDto boardDto = boardService.findById(id);

        // 댓글 목록 가져오기
        List<CommentDto> commentDtoList = commentService.findAll(id);

        // 게시글 조회하는 사람이 작성자인 경우
        if (loggedInUser.getUserCode().equals(boardDto.getUserCode())) {
            isWriter = true;
        }

        String formattedCreatedTime = boardDto.getBoardCreatedTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        model.addAttribute("board", boardDto);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("boardType", type);
        model.addAttribute("commentList", commentDtoList);
        model.addAttribute("formattedCreatedTime", formattedCreatedTime);
        model.addAttribute("isWriter", isWriter);
        model.addAttribute("user", loggedInUser);

        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDto boardDto = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDto);
        return "update";
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @ModelAttribute BoardUpdateDto boardUpdateDto, Model model)
    throws IOException {
        BoardDto updatedBoardDto = boardService.update(id, boardUpdateDto);
        model.addAttribute("board", updatedBoardDto);
        return ResponseEntity.ok(updatedBoardDto);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/";
    }
}
