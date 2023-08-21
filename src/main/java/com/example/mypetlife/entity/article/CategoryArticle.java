package com.example.mypetlife.entity.article;

public enum CategoryArticle {
    INFORMATION, // 정보
    NOTICE, // 공지
    QUESTION, // 질문
    CHAT; // 친목

    public String getCategoryName(CategoryArticle categoryArticle) {
        return categoryArticle.name().toLowerCase();
    }
}
