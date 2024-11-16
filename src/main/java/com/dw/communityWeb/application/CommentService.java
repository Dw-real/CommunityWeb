package com.dw.communityWeb.application;

import com.dw.communityWeb.domain.Board;
import com.dw.communityWeb.domain.Comment;
import com.dw.communityWeb.domain.User;
import com.dw.communityWeb.infrastructure.BoardRepository;
import com.dw.communityWeb.infrastructure.CommentRepository;
import com.dw.communityWeb.infrastructure.UserRepository;
import com.dw.communityWeb.presentation.dto.comment.CommentDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public Long save(CommentDto commentDto) {
        Optional<User> userEntity = userRepository.findById(commentDto.getUserCode());
        Optional<Board> boardEntity = boardRepository.findById(commentDto.getBoardId());

        if (userEntity.isPresent() && boardEntity.isPresent()) {
            Comment comment = Comment.toEntity(commentDto, boardEntity.orElse(null), userEntity.orElse(null));
            return commentRepository.save(comment).getId();
        } else {
            return null;
        }

    }

    @Transactional
    public List<CommentDto> findAll(Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        List<Comment> commentList = commentRepository.findAllByBoardOrderByIdDesc(board);

        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDto commentDto = CommentDto.toDto(comment);
            commentDtoList.add(commentDto);
        }

        return commentDtoList;
    }
}
