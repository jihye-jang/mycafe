package com.cafe.cafesite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cafe.cafesite.entity.Board;
import com.cafe.cafesite.entity.Category;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    
    Page<Board> findByCategory(Category category, Pageable pageable);
    
    Page<Board> findByTitleContaining(String title, Pageable pageable);
    
    Page<Board> findByContentContaining(String content, Pageable pageable);
    
    @Query("SELECT b FROM Board b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword%")
    Page<Board> findByTitleContainingOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT b FROM Board b WHERE b.author.nickname LIKE %:nickname%")
    Page<Board> findByAuthorNicknameContaining(@Param("nickname") String nickname, Pageable pageable);
    
    @Modifying
    @Transactional
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
    void incrementViewCount(@Param("id") Long id);
}