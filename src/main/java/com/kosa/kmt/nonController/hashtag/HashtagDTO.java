package com.kosa.kmt.nonController.hashtag;

import com.kosa.kmt.nonController.post.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HashtagDTO {
    String name;
    Long id;
    Integer count;
    List<Post> linkedPosts;

    public HashtagDTO(String name, Long id) {
        this.name = name;
        this.id = id;
        this.count = 0;
        this.linkedPosts = new ArrayList<Post>();
    }

    public void addPosts(Post post) {
        if(!linkedPosts.contains(post)) {
            this.linkedPosts.add(post);
            this.count += 1;
        }
    }

}
