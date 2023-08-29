package com.example.mypetlife.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ArticleViewController {

    /**
     * [GET] /community
     * 전체 게시글 조회
     */
    @GetMapping("/community")
    public String articleForm() {

        log.info("article view controller 실행");
        return "/article";
    }
}
