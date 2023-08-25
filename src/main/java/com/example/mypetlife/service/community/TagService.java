package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.community.article.Tag;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.repository.community.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    /*
     * DB에 없는 새로운 태그인지 확인
     */
    public boolean isNewTag(String tagName) {

        return !tagRepository.findByTagName(tagName)
                .isPresent();
    }

    /*
     * 태그이름으로 조회
     */
    public Tag findByTagName(String tagName) {

        return tagRepository.findByTagName(tagName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TAG));
    }

    /*
     * 저장
     */
    @Transactional
    public void saveTag(Tag tag) {

        tagRepository.save(tag);
    }
}
