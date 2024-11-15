package com.dw.communityWeb.presentation.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PwdUpdateDto {
    private String currentPwd;
    private String newPwd;
}
