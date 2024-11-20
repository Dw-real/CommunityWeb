package com.dw.communityWeb.presentation.controller;

import com.dw.communityWeb.application.UserService;
import com.dw.communityWeb.domain.user.User;
import com.dw.communityWeb.presentation.dto.login.PwdUpdateDto;
import com.dw.communityWeb.presentation.dto.user.UserDto;
import com.dw.communityWeb.presentation.dto.user.UserIdRequestDto;
import com.dw.communityWeb.presentation.dto.user.UserPwdRequestDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createForm() {
        return "create";
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody UserDto userDto) {
        try {
            userService.create(userDto);  // 사용자 생성 로직
            return ResponseEntity.ok("Account created successfully");  // 성공 응답
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while creating account: " + e.getMessage());  // 오류 응답
        }
    }

    @GetMapping("/id-check")
    public @ResponseBody String idCheck(@RequestParam("id") String id) {
        return userService.checkId(id);
    }

    @GetMapping("/findId")
    public String findIdForm() {
        return "findId";
    }

    @PostMapping("/findId")
    public ResponseEntity<?> findId(@RequestBody UserIdRequestDto userIdRequestDto) {
        User user = userService.findByInfo(userIdRequestDto);

        if (user != null) {
            return ResponseEntity.ok(user.getId());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("해당 정보로 가입된 ID가 존재하지 않습니다.");
        }
    }

    @GetMapping("/findPwd")
    public String findPwdForm() {
        return "findPwd";
    }

    @PostMapping("/findPwd")
    public ResponseEntity<?> findPwd(@RequestBody UserPwdRequestDto userPwdRequestDto) {
        User user = userService.findPwd(userPwdRequestDto);

        if (user != null) {
            return ResponseEntity.ok(user.getPwd());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("해당 정보로 가입된 정보가 존재하지 않습니다.");
        }
    }

    @GetMapping("/updatePwd")
    public String updateForm() {
        return "updatePwd";
    }

    @PatchMapping("/updatePwd")
    public ResponseEntity<?> updatePwd(HttpSession session, @RequestBody PwdUpdateDto pwdUpdateDto) {
        UserDto userDto = (UserDto) session.getAttribute("loggedInUser");

        boolean isUpdated = userService.updatePwd(userDto.getId(), pwdUpdateDto);
        if (!isUpdated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 변경에 실패했습니다.");
        }

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
