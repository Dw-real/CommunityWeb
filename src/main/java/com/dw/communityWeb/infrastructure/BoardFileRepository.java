package com.dw.communityWeb.infrastructure;

import com.dw.communityWeb.domain.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    @Transactional
    void deleteByStoredFileName(String StoredFileName);
    List<BoardFile> findByBoardId(Long boardId);
}
