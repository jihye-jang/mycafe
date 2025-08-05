package com.cafe.cafesite.controller;

import com.cafe.cafesite.entity.Board;
import com.cafe.cafesite.entity.User;
import com.cafe.cafesite.service.BoardService;
import com.cafe.cafesite.service.CategoryService;
import com.cafe.cafesite.service.CommentService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    
    private final BoardService boardService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    
    @GetMapping
    public String boardList(@RequestParam(required = false) Long categoryId,
                           @RequestParam(required = false) String searchType,
                           @RequestParam(required = false) String keyword,
                           @PageableDefault(size = 10, sort = "createdAt", 
                                         direction = Sort.Direction.DESC) Pageable pageable,
                           Model model) {
        
        var boards = (categoryId != null) ? 
            boardService.getBoardsByCategory(categoryId, pageable) :
            (keyword != null && !keyword.trim().isEmpty()) ?
                boardService.searchBoards(searchType, keyword, pageable) :
                boardService.getAllBoards(pageable);
        
        model.addAttribute("boards", boards);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("currentCategory", categoryId);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        
        return "board/list";
    }
    
    @GetMapping("/{id}")
    public String boardDetail(@PathVariable Long id, Model model) {
        var board = boardService.getBoardById(id);
        var comments = commentService.getCommentsByBoard(id);
        
        model.addAttribute("board", board);
        model.addAttribute("comments", comments);
        
        return "board/detail";
    }
    
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("board", new Board());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "board/write";
    }
    
    @PostMapping("/write")
    public String write(@ModelAttribute Board board,
                       @RequestParam Long categoryId,
                       @AuthenticationPrincipal User currentUser) {
        
        var savedBoard = boardService.createBoard(board, currentUser, categoryId);
        return "redirect:/board/" + savedBoard.getId();
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, 
                          @AuthenticationPrincipal User currentUser,
                          Model model) {
        var board = boardService.getBoardById(id);
        
        // 작성자 확인
        if (!board.getAuthor().getId().equals(currentUser.getId())) {
            return "redirect:/board/" + id + "?error=unauthorized";
        }
        
        model.addAttribute("board", board);
        model.addAttribute("categories", categoryService.getAllCategories());
        
        return "board/edit";
    }
    
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                      @ModelAttribute Board board,
                      @AuthenticationPrincipal User currentUser) {
        
        boardService.updateBoard(id, board, currentUser);
        return "redirect:/board/" + id;
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                        @AuthenticationPrincipal User currentUser) {
        
        boardService.deleteBoard(id, currentUser);
        return "redirect:/board";
    }
}