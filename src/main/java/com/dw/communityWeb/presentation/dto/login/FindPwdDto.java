package com.dw.communityWeb.presentation.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class FindPwdDto {
    private String id;
    private String newPwd;
}
