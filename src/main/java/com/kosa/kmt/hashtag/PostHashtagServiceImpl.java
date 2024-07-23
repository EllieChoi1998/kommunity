package com.kosa.kmt.hashtag;

import com.kosa.kmt.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    private PostHashtag saveHashtag(Post post, String hashtagStr) {
        Hashtag hashtag = hashtagService.save(hashtagStr);

        Optional<PostHashtag> optPostHashtag = postHashtagRepository.findByPost_IdAndHashtag_Id(post.getId(), hashtag.getId());

        if(optPostHashtag.isPresent()) {
            return optPostHashtag.get();
        }

        PostHashtag postHashtag = new PostHashtag();
        postHashtag.setPost(post);
        postHashtag.setHashtag(hashtag);

        postHashtagRepository.save(postHashtag);

        return postHashtag;
    }
}
