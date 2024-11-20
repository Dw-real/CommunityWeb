package com.dw.communityWeb.domain.board;

import com.dw.communityWeb.domain.Base;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table
public class BoardFile extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public static BoardFile toBoardFile(Board board, String originalFileName, String storedFileName) {
        BoardFile boardFile = new BoardFile();
        boardFile.setOriginalFileName(originalFileName);
        boardFile.setStoredFileName(storedFileName);
        boardFile.setBoard(board);

        return boardFile;
    }
}