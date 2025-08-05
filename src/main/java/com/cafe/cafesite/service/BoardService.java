package com.cafe.cafesite.service;

import com.cafe.cafesite.entity.Board;
import com.cafe.cafesite.entity.Category;
import com.cafe.cafesite.entity.User;
import com.cafe.cafesite.repository.BoardRepository;
import com.cafe.cafesite.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    
    public Page<Board> getAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }
    
    public Page<Board> getBoardsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
        return boardRepository.findByCategory(category, pageable);
    }
    
    public Board getBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        // 조회수 증가
        boardRepository.incrementViewCount(id);
        board.setViewCount(board.getViewCount() + 1);
        
        return board;
    }
    
    public Board createBoard(Board board, User author, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
        
        board.setAuthor(author);
        board.setCategory(category);
        
        return boardRepository.save(board);
    }
    
    public Board updateBoard(Long id, Board updatedBoard, User currentUser) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        // 작성자 확인
        if (!board.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }
        
        board.setTitle(updatedBoard.getTitle());
        board.setContent(updatedBoard.getContent());
        board.setUpdatedAt(java.time.LocalDateTime.now());
        
        return boardRepository.save(board);
    }
    
    public void deleteBoard(Long id, User currentUser) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        // 작성자 또는 관리자 확인
        if (!board.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        
        boardRepository.delete(board);
    }
    
    public Page<Board> searchBoards(String type, String keyword, Pageable pageable) {
        switch (type) {
            case "title":
                return boardRepository.findByTitleContaining(keyword, pageable);
            case "content":
                return boardRepository.findByContentContaining(keyword, pageable);
            case "author":
                return boardRepository.findByAuthorNicknameContaining(keyword, pageable);
            case "all":
                return boardRepository.findByTitleContainingOrContentContaining(keyword, pageable);
            default:
                return boardRepository.findAll(pageable);
        }
    }
}