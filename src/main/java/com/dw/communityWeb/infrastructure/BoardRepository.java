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

    Page<Board> findByBoardType(Type boardType, Pageable pageable);

    Page<Board> findByUser_UserCode(Long userCode, Pageable pageable);
}
