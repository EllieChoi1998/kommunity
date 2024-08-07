package com.kosa.kmt.nonController.post.hateOrLike;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class


PostLikeOrHateServiceImpl implements PostLikeOrHateService {

    private final PostLikeRepository postLikeRepository;
    private final PostHateRepository postHateRepository;

    @Override
    public void likePost(Post post, Member member) {
        PostLike existingLike = postLikeRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId());
        if (existingLike != null) {
            postLikeRepository.delete(existingLike);
        } else {
            PostHate existingHate = postHateRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId());
            if (existingHate != null) {
                postHateRepository.delete(existingHate);
            }
            PostLike postLike = new PostLike();
            postLike.setPost(post);
            postLike.setMember(member);
            postLikeRepository.save(postLike);
        }
    }

    @Override
    public void hatePost(Post post, Member member) {
        PostHate existingHate = postHateRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId());
        if (existingHate != null) {
            postHateRepository.delete(existingHate);
        } else {
            PostLike existingLike = postLikeRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId());
            if (existingLike != null) {
                postLikeRepository.delete(existingLike);
            }
            PostHate postHate = new PostHate();
            postHate.setPost(post);
            postHate.setMember(member);
            postHateRepository.save(postHate);
        }
    }

}
