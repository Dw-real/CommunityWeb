package com.dw.communityWeb.presentation.dto.board;

import com.dw.communityWeb.domain.board.Board;
import com.dw.communityWeb.domain.board.BoardFile;
import com.dw.communityWeb.domain.board.Type;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDto {
    private Long id;
    private Type boardType;
    private String boardWriter;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;
    private Long userCode;

    private List<MultipartFile> boardFile; // 파일 담는 용도
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 서버 저장용 파일 이름
    private int fileAttached;
    private int commentCount;

    public BoardDto(Long id, Type boardType, String boardWriter, String boardTitle, int boardHits,
                    LocalDateTime boardCreatedTime, int commentCount) {
        this.id = id;
        this.boardType = boardType;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
        this.commentCount = commentCount;
    }

    public static BoardDto toDto(Board board) {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(board.getId());
        boardDto.setBoardType(board.getBoardType());
        boardDto.setBoardWriter(board.getBoardWriter());
        boardDto.setBoardTitle(board.getBoardTitle());
        boardDto.setBoardContents(board.getBoardContents());
        boardDto.setBoardHits(board.getBoardHits());
        boardDto.setBoardCreatedTime(board.getCreatedTime());
        boardDto.setBoardUpdatedTime(board.getUpdatedTime());
        boardDto.setUserCode(board.getUser().getUserCode());

        if (board.getFileAttached() == 0) {
            boardDto.setFileAttached(0);
        } else {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            boardDto.setFileAttached(1);

            for (BoardFile boardFile : board.getBoardFileList()) {
                originalFileNameList.add(boardFile.getOriginalFileName());
                storedFileNameList.add(boardFile.getStoredFileName());
            }
            boardDto.setOriginalFileName(originalFileNameList);
            boardDto.setStoredFileName(storedFileNameList);
        }

        return boardDto;
    }
}
