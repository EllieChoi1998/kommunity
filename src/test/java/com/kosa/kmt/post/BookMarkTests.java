package com.kosa.kmt.post;

import com.kosa.kmt.member.Member;
import com.kosa.kmt.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BookMarkTests {

    @Autowired
    private BookMarkService bookMarkService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Post post;

    @BeforeEach
    public void setUp() {
        this.member = new Member();
        this.member.setName("John Doe");
        this.member.setEmail("john.doe@gmail.com");
        this.member.setNickname("JD");
        this.member.setPassword("1234");
        this.member = this.memberRepository.save(this.member);

        post = new Post();
        post.setTitle("Test Post");
        post.setContent("This is a test post.");
        postRepository.save(post);
    }

    @Test
    public void testToggleBookMark() {
        // 북마크 추가
        int result1 = bookMarkService.toggleBookMark(post, member);
        assertEquals(1, result1, "북마크가 추가되어야 합니다.");

        // 북마크가 추가되었는지 확인
        assertTrue(bookMarkService.isBookMarkedByMember(post, member), "북마크가 추가된 상태여야 합니다.");

        // 북마크 개수 확인 (추가 후)
        List<BookMark> bookMarksAfterAdd = bookMarkService.getBookMarksByMember(member);
        assertEquals(1, bookMarksAfterAdd.size(), "북마크가 하나여야 합니다.");

        // 다시 북마크 버튼을 눌러 북마크 제거
        int result2 = bookMarkService.toggleBookMark(post, member);
        assertEquals(0, result2, "북마크가 제거되어야 합니다.");

        // 북마크가 제거되었는지 확인
        assertFalse(bookMarkService.isBookMarkedByMember(post, member), "북마크가 제거된 상태여야 합니다.");

        // 북마크 개수 확인 (제거 후)
        List<BookMark> bookMarksAfterRemove = bookMarkService.getBookMarksByMember(member);
        assertEquals(0, bookMarksAfterRemove.size(), "북마크가 없어야 합니다.");
    }

    @Test
    public void testCountBookMarksByPost() {
        // 북마크 추가
        bookMarkService.toggleBookMark(post, member);

        // 북마크 개수 확인
        long count = bookMarkService.countBookMarksByPost(post);
        assertEquals(1, count, "북마크 개수는 1이어야 합니다.");

        // 북마크 제거
        bookMarkService.toggleBookMark(post, member);

        // 북마크 개수 확인 (제거 후)
        count = bookMarkService.countBookMarksByPost(post);
        assertEquals(0, count, "북마크 개수는 0이어야 합니다.");
    }

}
