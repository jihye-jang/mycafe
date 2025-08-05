package com.cafe.cafesite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cafe.cafesite.service.BoardService;
import com.cafe.cafesite.service.CategoryService;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final BoardService boardService;
    private final CategoryService categoryService;
    
    @GetMapping("/")
    public String home(Model model) {
        // 최신 게시글 5개
        var recentBoards = boardService.getAllBoards(
            PageRequest.of(0, 5, Sort.by("createdAt").descending())
        );
        
        model.addAttribute("recentBoards", recentBoards.getContent());
        model.addAttribute("categories", categoryService.getAllCategories());
        
        return "index";
    }
}