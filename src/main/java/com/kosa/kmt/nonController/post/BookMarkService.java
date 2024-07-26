package com.kosa.kmt.nonController.post;

import com.kosa.kmt.nonController.member.Member;

import java.util.List;

public interface BookMarkService {

    int toggleBookMark(Post post, Member member);

    void removeBookMark(Post post, Member member);

    List<BookMark> getBookMarksByMember(Member member);

    List<BookMark> getBookMarksByPost(Post post);

    long countBookMarksByPost(Post post);

    boolean isBookMarkedByMember(Post post, Member member);

}