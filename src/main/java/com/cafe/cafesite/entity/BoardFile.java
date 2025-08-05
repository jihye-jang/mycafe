package com.cafe.cafesite.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String originalName;
    private String savedName;
    private String filePath;
    private long fileSize;
    private String contentType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
    
    private LocalDateTime uploadedAt = LocalDateTime.now();
} 