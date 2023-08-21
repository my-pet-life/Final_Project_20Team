package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.article.Tag;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public boolean isExistInDb(String tagName) {

        Optional<Tag> optionalTag = tagRepository.findByTagName(tagName);
        if(optionalTag.isEmpty()) {
            return false;
        }
        return true;
    }

    public Tag findByTagName(String tagName) {
        return tagRepository.findByTagName(tagName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TAG));
    }

    public void saveTag(Tag tag) {
        tagRepository.save(tag);
    }
}
