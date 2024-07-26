package com.kosa.kmt.non_controller.post;

import com.kosa.kmt.non_controller.member.Member;

import java.util.List;

public interface BookMarkService {

    int toggleBookMark(Post post, Member member);

    void removeBookMark(Post post, Member member);

    List<BookMark> getBookMarksByMember(Member member);

    List<BookMark> getBookMarksByPost(Post post);

    long countBookMarksByPost(Post post);

    boolean isBookMarkedByMember(Post post, Member member);

}