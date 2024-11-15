package com.dw.communityWeb.application;

import com.dw.communityWeb.domain.Board;
import com.dw.communityWeb.domain.BoardFile;
import com.dw.communityWeb.domain.User;
import com.dw.communityWeb.infrastructure.BoardFileRepository;
import com.dw.communityWeb.infrastructure.BoardRepository;
import com.dw.communityWeb.infrastructure.UserRepository;
import com.dw.communityWeb.presentation.dto.board.BoardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final UserRepository userRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, BoardFileRepository boardFileRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.boardFileRepository = boardFileRepository;
        this.userRepository = userRepository;
    }

    public void post(BoardDto boardDto) throws IOException {
        Optional<User> userEntity = userRepository.findById(boardDto.getUserCode());

        if (userEntity.isPresent()) {
            if (boardDto.getBoardFile() == null) {
                Board board = Board.toEntity(boardDto, userEntity.orElse(null));
                boardRepository.save(board);
            } else {
                // 첨부 파일 있음
                /*
                    1. DTO에 담긴 파일을 꺼냄
                    2. 파일의 이름 가져옴
                    3. 서버 저장용 이름 생성
                    4. 저장 경로 설정
                    5. 해당 경로에 파일 저장
                    6. board_table에 해당 데이터 save 처리
                    7  board_file_table에 해당 데이터 save 처리
                */
                Board board = Board.toFileEntity(boardDto, userEntity.orElse(null));
                Long savedId = boardRepository.save(board).getId();
                Board savedBoard = boardRepository.findById(savedId).get();

                for (MultipartFile boardFile : boardDto.getBoardFile()) {
                    String originalFileName = boardFile.getOriginalFilename();
                    String storedFileName = System.currentTimeMillis() + " " + originalFileName;
                    String savePath = "/Users/handongwoo/communityFile/" + storedFileName;
                    boardFile.transferTo(new File(savePath));

                    BoardFile savedboardFile = BoardFile.toBoardFile(savedBoard, originalFileName, storedFileName);
                    boardFileRepository.save(savedboardFile);
                }
            }
        }
    }

    public Page<BoardDto> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10; // 한 페이지에 보여줄 글 개수

        Page<Board> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        Page<BoardDto> boardDtos = boardEntities.map(board -> new BoardDto(board.getId(), board.getBoardWriter(), board.getBoardTitle(),
                board.getBoardHits(), board.getCreatedTime()));

        return boardDtos;
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional
    public BoardDto findById(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            BoardDto boardDto = BoardDto.toDto(board);
            return boardDto;
        } else {
            return null;
        }
    }

    @Transactional
    public BoardDto update(Long id, BoardDto boardDto) {
        Board existBoard = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));

        User existUser = userRepository.findById(boardDto.getUserCode())
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        Board updatedBoard = Board.updateEntity(boardDto, existUser);
        boardRepository.save(updatedBoard);

        return BoardDto.toDto(updatedBoard);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}
