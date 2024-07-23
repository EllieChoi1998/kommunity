package com.kosa.kmt.hashtag;

import com.kosa.kmt.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostHashtagServiceImpl implements PostHashtagService {

    private final HashtagService hashtagService;

    private final PostHashtagRepository postHashtagRepository;

    @Override
    public void setHashtag(Post post, String hashtagStr) {
        List<String> hashtagList = Arrays.stream(hashtagStr.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        hashtagList.forEach(hashtag -> {
            saveHashtag(post, hashtag);
        });

    }

    private void saveHashtag(Post post, String hashtagStr) {
        Hashtag hashtag = hashtagService.save(hashtagStr);

        postHashtagRepository.findByPostAndHashtag(post.getId(), hashtag.getId());

    }
}
