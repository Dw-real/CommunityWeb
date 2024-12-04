package com.dw.communityWeb.infrastructure;

import com.dw.communityWeb.domain.board.Board;
import com.dw.communityWeb.domain.board.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Modifying
    @Query(value = "update Board b set b.boardHits=b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);

    // 전체 게시글에 대한 댓글 개수를 가져오는 쿼리
    @Query("SELECT b, COUNT(c) FROM Board b LEFT JOIN Comment c ON b.id = c.board.id GROUP BY b")
    Page<Object[]> findBoardsWithCommentCount(Pageable pageable);

    // 특정 타입의 게시글에 대해 댓글 개수를 가져오는 쿼리
    @Query("SELECT b, COUNT(c) FROM Board b LEFT JOIN Comment c ON b.id = c.board.id WHERE b.boardType = :boardType GROUP BY b")
    Page<Object[]> findBoardsWithCommentCountByType(@Param("boardType") Type boardType, Pageable pageable);

    @Query("SELECT b, COUNT(c) FROM Board b LEFT JOIN Comment c ON b.id = c.board.id WHERE b.user.userCode = :userCode GROUP BY b")
    Page<Object[]> findBoardsWithCommentCountByUserCode(@Param("userCode") Long userCode, Pageable pageable);
}
