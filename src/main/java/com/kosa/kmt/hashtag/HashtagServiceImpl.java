package com.kosa.kmt.hashtag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    @Override
    public Hashtag save(String hashtagStr) {
        Optional<Hashtag> opHashtag = hashtagRepository.findByContent(hashtagStr);

        if(opHashtag.isPresent()) {
            return opHashtag.get();
        }

        Hashtag hashtag = new Hashtag();
        hashtag.setName(hashtagStr);
        hashtagRepository.save(hashtag);

        return hashtag;
    }
}
