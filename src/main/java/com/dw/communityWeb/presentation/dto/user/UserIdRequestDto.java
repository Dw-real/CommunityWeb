package com.dw.communityWeb.presentation.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserIdRequestDto {
    private String name;
    private String email;
}
