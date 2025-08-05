package com.cafe.cafesite.service;

import com.cafe.cafesite.entity.Board;
import com.cafe.cafesite.entity.Comment;
import com.cafe.cafesite.entity.User;
import com.cafe.cafesite.repository.BoardRepository;
import com.cafe.cafesite.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    
    public List<Comment> getCommentsByBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        return commentRepository.findByBoardAndParentIsNullOrderByCreatedAtAsc(board);
    }
    
    public Comment createComment(Long boardId, String content, User author, Long parentId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setBoard(board);
        
        if (parentId != null) {
            Comment parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));
            comment.setParent(parent);
        }
        
        return commentRepository.save(comment);
    }
    
    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        
        // 작성자 또는 관리자 확인
        if (!comment.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        
        commentRepository.delete(comment);
    }
}