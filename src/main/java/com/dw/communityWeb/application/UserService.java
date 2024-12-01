package com.dw.communityWeb.application;

import com.dw.communityWeb.domain.user.User;
import com.dw.communityWeb.infrastructure.UserRepository;
import com.dw.communityWeb.presentation.dto.login.PwdUpdateDto;
import com.dw.communityWeb.presentation.dto.user.UserDto;
import com.dw.communityWeb.presentation.dto.user.UserIdRequestDto;
import com.dw.communityWeb.presentation.dto.user.UserPwdRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public boolean logIn(String userId, String userPwd) {
        User user = userRepository.findUserById(userId);
        return user != null && bCryptPasswordEncoder.matches(userPwd, user.getPwd());
    }

    public void create(UserDto userDto) {
        User user = UserDto.toEntity(userDto);
        String encodedPwd = bCryptPasswordEncoder.encode(userDto.getPwd());
        user.setPwd(encodedPwd);
        userRepository.save(user);
    }

    public String checkId(String id) {
        Long count = userRepository.checkId(id);

        if (count > 0) {
            return "no";
        } else {
            return "ok";
        }
    }

    public UserDto findById(String id) {
        User user = userRepository.findUserById(id);
        UserDto userDto = UserDto.toDto(user);

        return userDto;
    }

    public User findByInfo(UserIdRequestDto userIdRequestDto) {
        return userRepository.findByInfo(userIdRequestDto.getName(), userIdRequestDto.getEmail());
    }

    public User findPwd(UserPwdRequestDto userPwdRequestDto) {
        return userRepository.findPwd(userPwdRequestDto.getId(), userPwdRequestDto.getEmail());
    }

    @Transactional
    public boolean updatePwd(String id, PwdUpdateDto pwdUpdateDto) {
        User user = userRepository.findUserById(id);

        // 현재 비밀번호가 일치하는지 확인
        if (!bCryptPasswordEncoder.matches(pwdUpdateDto.getCurrentPwd(), user.getPwd())) {
            return false;  // 비밀번호가 일치하지 않으면 false 반환
        }

        // 새 비밀번호로 업데이트
        String encodedNewPwd = bCryptPasswordEncoder.encode(pwdUpdateDto.getNewPwd());  // 새 비밀번호 암호화
        userRepository.updatePwd(id, encodedNewPwd);  // 암호화된 새 비밀번호로 업데이트
        return true;
    }

    @Transactional
    public boolean updatePwd(String id, String newPwd) {
        User user = userRepository.findUserById(id);

        // 새 비밀번호로 업데이트
        String encodedNewPwd = bCryptPasswordEncoder.encode(newPwd);  // 새 비밀번호 암호화
        userRepository.updatePwd(id, encodedNewPwd);  // 암호화된 새 비밀번호로 업데이트
        return true;
    }
}
