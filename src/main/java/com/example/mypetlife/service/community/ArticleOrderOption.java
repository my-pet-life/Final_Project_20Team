package com.example.mypetlife.service.community;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum ArticleOrderOption {

    LATEST(Sort.by(Sort.Direction.DESC, "createdDate")),
    LIKE(Sort.by(Sort.Direction.DESC, "likeCount").and(Sort.by(Sort.Direction.DESC, "createdDate"))),
    COMMENT(Sort.by(Sort.Direction.DESC, "commentCount").and(Sort.by(Sort.Direction.DESC, "createdDate")));

    private final Sort sort;

    public static Sort getSortFromOrder(String order) {

        return Arrays.stream(values())
                .filter(orderOption -> orderOption.name().equals(order.toUpperCase(Locale.ROOT)))
                .findFirst()
                .orElse(LATEST)
                .sort;
    }
}
