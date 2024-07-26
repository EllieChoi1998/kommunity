package com.kosa.kmt.nonController.hashtag;

import com.kosa.kmt.nonController.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostHashtagServiceImpl implements PostHashtagService {

    private final HashtagService hashtagService;

    private final PostHashtagRepository postHashtagRepository;

    @Transactional
    @Override
    public void setHashtag(Post post, String hashtagStr) {

        List<PostHashtag> previousHashtags = getPostHashtags(post);

        List<String> hashtagList = Arrays.stream(hashtagStr.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        List<PostHashtag> needToDelete = new ArrayList<>();

        for (PostHashtag preHashtag : previousHashtags) {
            preHashtag.getHashtag().getName();

            boolean contains = hashtagList.stream()
                    .anyMatch(s -> s.equals(preHashtag.getHashtag().getName()));
            if (!contains) {
                needToDelete.add(preHashtag);
            }
        }

        hashtagList.forEach(hashtag -> {
            saveHashtag(post, hashtag);
        });

        needToDelete.forEach(hashtag -> {
            postHashtagRepository.delete(hashtag);
        });
    }

    @Override
    public List<PostHashtag> getPostHashtags(Post post) {
        return postHashtagRepository.findAllByPost_Id(post.getId());
    }

    private PostHashtag saveHashtag(Post post, String hashtagStr) {
        Hashtag hashtag = hashtagService.save(hashtagStr);

        Optional<PostHashtag> optPostHashtag = postHashtagRepository.findByPost_IdAndHashtag_Id(post.getId(), hashtag.getId());

        if (optPostHashtag.isPresent()) {
            return optPostHashtag.get();
        }

        PostHashtag postHashtag = new PostHashtag();
        postHashtag.setPost(post);
        postHashtag.setHashtag(hashtag);

        postHashtagRepository.save(postHashtag);

        return postHashtag;
    }
}
