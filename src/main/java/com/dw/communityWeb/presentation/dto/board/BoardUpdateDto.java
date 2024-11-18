package com.dw.communityWeb.presentation.dto.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor
public class BoardUpdateDto {
    private Long boardId;
    private String boardTitle;
    private String boardContents;
    private Long userCode;
    private int boardHits;
    private List<MultipartFile> newFiles;
    private List<String> removedFiles;
}
