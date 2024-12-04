package com.dw.communityWeb.application;

import com.dw.communityWeb.domain.board.Board;
import com.dw.communityWeb.domain.board.BoardFile;
import com.dw.communityWeb.domain.board.Type;
import com.dw.communityWeb.domain.user.User;
import com.dw.communityWeb.infrastructure.BoardFileRepository;
import com.dw.communityWeb.infrastructure.BoardRepository;
import com.dw.communityWeb.infrastructure.CommentRepository;
import com.dw.communityWeb.infrastructure.UserRepository;
import com.dw.communityWeb.presentation.dto.board.BoardUpdateDto;
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
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, BoardFileRepository boardFileRepository, UserRepository userRepository,
                        CommentRepository commentRepository) {
        this.boardRepository = boardRepository;
        this.boardFileRepository = boardFileRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public void post(BoardDto boardDto) throws IOException {
        Optional<User> userEntity = userRepository.findById(boardDto.getUserCode());

        if (userEntity.isPresent()) {
            if (boardDto.getBoardFile() == null || boardDto.getBoardFile().isEmpty()) {
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

    public Page<BoardDto> paging(String type, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        Pageable pageAble = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));

        Page<Object[]> result = null;

        if ("allBoard".equals(type)) {
            result = boardRepository.findBoardsWithCommentCount(pageAble);
        } else if ("freeBoard".equals(type)) {
            result = boardRepository.findBoardsWithCommentCountByType(Type.FREE, pageAble);
        } else {
            result = boardRepository.findBoardsWithCommentCountByType(Type.QNA, pageAble);
        }

        Page<BoardDto> boardDtos = result.map(objects -> {
            Board board = (Board) objects[0];  // 첫 번째 객체는 Board 엔티티
            Long commentCount = (Long) objects[1];  // 두 번째 객체는 댓글 개수
            return new BoardDto(board.getId(), board.getBoardType(), board.getBoardWriter(),
                    board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime(), commentCount);
        });

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
    public BoardDto update(Long id, BoardUpdateDto boardUpdateDto) throws IOException {
        Board existBoard = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));

        User existUser = userRepository.findById(boardUpdateDto.getUserCode())
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        Board updatedBoard = Board.updateEntity(boardUpdateDto, existUser);

        // 삭제한 파일이 있는 경우
        if (boardUpdateDto.getRemovedFiles() != null) {
            List<String> filesToDelete = boardUpdateDto.getRemovedFiles();
            for (String fileName : filesToDelete) {
                boardFileRepository.deleteByStoredFileName(fileName);
            }
            if (!boardFileRepository.findByBoardId(boardUpdateDto.getBoardId()).isEmpty()) {
                updatedBoard.setFileAttached(1);
            }
            else {
                updatedBoard.setFileAttached(0);
            }
        }

        // 추가한 파일이 있는 경우
        if (boardUpdateDto.getNewFiles() != null && !boardUpdateDto.getNewFiles().isEmpty()) {
            updatedBoard.setFileAttached(1);
            for (MultipartFile newFile : boardUpdateDto.getNewFiles()) {
                String originalFileName = newFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + " " + originalFileName;
                String savePath = "/Users/handongwoo/communityFile/" + storedFileName;
                newFile.transferTo(new File(savePath));

                BoardFile savedboardFile = BoardFile.toBoardFile(updatedBoard, originalFileName, storedFileName);
                boardFileRepository.save(savedboardFile);
            }
        }

        boardRepository.save(updatedBoard);

        return BoardDto.toDto(updatedBoard);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDto> getMyPosts(Long userCode, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        Pageable pageAble = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));

        Page<Object[]> result = boardRepository.findBoardsWithCommentCountByUserCode(userCode, pageAble);

        Page<BoardDto> boardDtos = result.map(objects -> {
            Board board = (Board) objects[0];  // 첫 번째 객체는 Board 엔티티
            Long commentCount = (Long) objects[1];  // 두 번째 객체는 댓글 개수
            return new BoardDto(board.getId(), board.getBoardType(), board.getBoardWriter(),
                    board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime(), commentCount);
        });

        return boardDtos;
    }
}
