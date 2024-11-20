package com.dw.communityWeb.presentation.dto.comment;

import com.dw.communityWeb.domain.board.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
public class CommentDto {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private Long userCode;
    private LocalDateTime commentCreatedTime;

    public static CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setCommentWriter(comment.getUser().getId());
        commentDto.setCommentContents(comment.getCommentContents());
        commentDto.setBoardId(comment.getBoard().getId());
        commentDto.setUserCode(comment.getUser().getUserCode());
        commentDto.setCommentCreatedTime(comment.getCreatedTime());

        return commentDto;
    }
}
