package com.kosa.kmt.post;

import com.kosa.kmt.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookMarkServiceImpl implements BookMarkService {

    private final BookMarkRepository bookMarkRepository;

    @Override
    public int toggleBookMark(Post post, Member member) {
        if (isBookMarkedByMember(post, member)) {
            removeBookMark(post, member);
            return 0; // 북마크가 제거된 경우
        } else {
            BookMark bookMark = new BookMark();
            bookMark.setPost(post);
            bookMark.setMember(member);
            bookMarkRepository.save(bookMark);
            return 1; // 북마크가 추가된 경우
        }
    }

    @Override
    public void removeBookMark(Post post, Member member) {
        bookMarkRepository.deleteByPost_IdAndMember_MemberId(post.getId(), member.getMemberId());
    }

    @Override
    public List<BookMark> getBookMarksByMember(Member member) {
        return bookMarkRepository.findAllByMember_MemberId(member.getMemberId());
    }

    @Override
    public List<BookMark> getBookMarksByPost(Post post) {
        return bookMarkRepository.findAllByPost_Id(post.getId());
    }

    @Override
    public long countBookMarksByPost(Post post) {
        return bookMarkRepository.countBookMarksByPost(post);
    }

    @Override
    public boolean isBookMarkedByMember(Post post, Member member) {
        Optional<BookMark> existingBookMark = bookMarkRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId());
        return existingBookMark.isPresent();
    }
}
