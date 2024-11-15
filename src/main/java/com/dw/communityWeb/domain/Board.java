package com.dw.communityWeb.domain;

import com.dw.communityWeb.presentation.dto.board.BoardDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table
public class Board extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column
    private String boardWriter;

    @Column(nullable = false)
    private String boardTitle;

    @Column(length = 500, nullable = false)
    private String boardContents;

    @Column
    private int boardHits;

    private int fileAttached; // 파일이 있으면 1 없으면 0

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFile> boardFileList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    public static Board toEntity(BoardDto boardDto, User user) {
        Board board = new Board();
        board.setBoardWriter(user.getId());
        board.setBoardTitle(boardDto.getBoardTitle());
        board.setBoardContents(boardDto.getBoardContents());
        board.setBoardHits(0);
        board.setFileAttached(0); // 파일 첨부 X
        board.setUser(user);

        return board;
    }

    public static Board toFileEntity(BoardDto boardDto, User user) {
        Board board = new Board();
        board.setBoardWriter(user.getId());
        board.setBoardTitle(boardDto.getBoardTitle());
        board.setBoardContents(boardDto.getBoardContents());
        board.setBoardHits(0);
        board.setFileAttached(1); // 파일 첨부 O
        board.setUser(user);

        return board;
    }

    public static Board updateEntity(BoardDto boardDto, User user) {
        Board board = new Board();
        board.setId(boardDto.getId());
        board.setBoardWriter(user.getId());
        board.setBoardTitle(boardDto.getBoardTitle());
        board.setBoardContents(boardDto.getBoardContents());
        board.setBoardHits(boardDto.getBoardHits());
        board.setUser(user);

        return board;
    }
}
