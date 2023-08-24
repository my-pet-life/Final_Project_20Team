package com.example.mypetlife.entity.article;

public enum ArticleCategory {
    INFORMATION, // 정보
    NOTICE, // 공지
    QUESTION, // 질문
    CHAT; // 친목

    public String getCategoryName(ArticleCategory articleCategory) {
        return articleCategory.name().toLowerCase();
    }
}
