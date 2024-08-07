package com.kosa.kmt.nonController.post.bookmark;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.post.Post;

import java.util.List;

public interface BookMarkService {

    int toggleBookMark(Post post, Member member);

    void removeBookMark(Post post, Member member);

    List<BookMark> getBookMarksByMember(Member member);

    long countBookMarksByPost(Post post);

    boolean isBookMarkedByMember(Post post, Member member);

}