package com.dw.communityWeb.domain;

import com.dw.communityWeb.presentation.dto.comment.CommentDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table
public class Comment extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column
    private String commentContents;

    //Board:Comment= 1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    // User:Comment=1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    public static Comment toEntity(CommentDto commentDto, Board board, User user) {
        Comment comment = new Comment();
        comment.setCommentWriter(user.getId());
        comment.setCommentContents(commentDto.getCommentContents());
        comment.setBoard(board);
        comment.setUser(user);

        return comment;
    }
}