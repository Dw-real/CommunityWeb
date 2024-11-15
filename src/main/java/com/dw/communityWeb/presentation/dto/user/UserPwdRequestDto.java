package com.dw.communityWeb.presentation.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserPwdRequestDto {
    private String id;
    private String email;
}
