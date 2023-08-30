package com.example.mypetlife.entity.community.article;

public enum ArticleCategory {

    NOTICE, // 공지
    REVIEW, // 리뷰
    QUESTION, // 질문
    CHAT; // 친목

    public String getCategoryName(ArticleCategory articleCategory) {
        return articleCategory.name().toLowerCase();
    }
}
