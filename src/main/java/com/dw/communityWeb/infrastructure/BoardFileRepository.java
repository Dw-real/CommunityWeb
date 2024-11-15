package com.dw.communityWeb.infrastructure;

import com.dw.communityWeb.domain.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
}
