package com.cafe.cafesite.controller;

import com.cafe.cafesite.entity.User;
import com.cafe.cafesite.service.CommentService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    @PostMapping("/write")
    public String writeComment(@RequestParam Long boardId,
                              @RequestParam String content,
                              @RequestParam(required = false) Long parentId,
                              @AuthenticationPrincipal User currentUser) {
        
        commentService.createComment(boardId, content, currentUser, parentId);
        return "redirect:/board/" + boardId;
    }
    
    @PostMapping("/{id}/delete")
    public String deleteComment(@PathVariable Long id,
                               @RequestParam Long boardId,
                               @AuthenticationPrincipal User currentUser) {
        
        commentService.deleteComment(id, currentUser);
        return "redirect:/board/" + boardId;
    }
}