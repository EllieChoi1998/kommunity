package com.kosa.kmt.controller;
import com.kosa.kmt.controller.PostController;
import com.kosa.kmt.nonController.hashtag.HashtagDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HashtagController {

    @GetMapping("/hashtags")
    public List<String> getHashtags() {
        // 예시: 해시태그 리스트를 반환합니다. 실제 로직에 맞게 수정해야 합니다.
        List<HashtagDTO> sortedHashtagDTO = PostController.staticHashtagDTOs;

        List<String> hashtagNames = new ArrayList<>();
        for (HashtagDTO hashtagDTO : sortedHashtagDTO) {
            hashtagNames.add(hashtagDTO.getName());
        }
        return hashtagNames;
    }
}
