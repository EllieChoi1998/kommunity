package com.kosa.kmt.non_controller.hashtag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    @Override
    public Hashtag save(String hashtagStr) {
        Optional<Hashtag> opHashtag = hashtagRepository.findByName(hashtagStr);

        if(opHashtag.isPresent()) {
            return opHashtag.get();
        }

        Hashtag hashtag = new Hashtag();
        hashtag.setName(hashtagStr);
        hashtagRepository.save(hashtag);

        return hashtag;
    }
}
