package com.cafe.cafesite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cafe.cafesite.entity.Board;
import com.cafe.cafesite.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardAndParentIsNullOrderByCreatedAtAsc(Board board);
    List<Comment> findByParentOrderByCreatedAtAsc(Comment parent);
    long countByBoard(Board board);
}