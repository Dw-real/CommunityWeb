package com.dw.communityWeb.infrastructure;

import com.dw.communityWeb.domain.Board;
import com.dw.communityWeb.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardOrderByIdDesc(Board board);
}
