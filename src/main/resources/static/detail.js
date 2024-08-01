document.addEventListener('DOMContentLoaded', (event) => {
    const deleteElements = document.querySelectorAll(".delete");
    const likeElements = document.querySelectorAll(".like");
    const dislikeElements = document.querySelectorAll(".dislike");
    const deletePostButtons = document.querySelectorAll(".delete-post");
    const bookmarkElements= document.querySelectorAll(".bookmark");

    // CSRF 토큰 설정
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    // 게시물 삭제 처리
    deletePostButtons.forEach(button => {
        button.addEventListener('click', function () {
            if (confirm("정말로 삭제하시겠습니까?")) {
                fetch(this.dataset.uri, {
                    method: 'DELETE',
                    headers: {
                        [csrfHeader]: csrfToken,
                        'Content-Type': 'application/json'
                    }
                }).then(response => {
                    if (response.ok) {
                        alert("삭제가 완료되었습니다.");
                        window.location.href = '/posts/board/1';
                    } else {
                        alert("삭제 실패");
                    }
                });
            }
        });
    });

    // 댓글 삭제 처리
    deleteElements.forEach(element => {
        element.addEventListener('click', function () {
            if (confirm("정말로 삭제하시겠습니까?")) {
                fetch(this.dataset.uri, {
                    method: 'POST',
                    headers: {
                        [csrfHeader]: csrfToken,
                        'Content-Type': 'application/json'
                    }
                }).then(response => {
                    if (response.ok) {
                        alert("삭제가 완료되었습니다.");
                        window.location.reload();
                    } else {
                        alert("삭제 실패");
                    }
                });
            }
        });
    });

    // 좋아요 처리
    likeElements.forEach(element => {
        element.addEventListener('click', function () {
            if (confirm("정말로 추천 하시겠습니까?")) {
                fetch(this.dataset.uri, {
                    method: 'POST',
                    headers: {
                        [csrfHeader]: csrfToken,
                        'Content-Type': 'application/json'
                    }
                }).then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        alert("추천 실패");
                    }
                });
            }
        });
    });

    // 비추천 처리
    dislikeElements.forEach(element => {
        element.addEventListener('click', function () {
            if (confirm("정말로 비추천 하시겠습니까?")) {
                fetch(this.dataset.uri, {
                    method: 'POST',
                    headers: {
                        [csrfHeader]: csrfToken,
                        'Content-Type': 'application/json'
                    }
                }).then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        alert("비추천 실패");
                    }
                });
            }
        });
    });


    bookmarkElements.forEach(element => {
        element.addEventListener('click', function () {

                fetch(this.dataset.uri, {
                    method: 'POST',
                    headers: {
                        [csrfHeader]: csrfToken,
                        'Content-Type': 'application/json'
                    }
                }).then(response => {
                    if (response.ok) {
                        alert("북마크 추가")
                        window.location.reload();
                    } else {
                        alert("북마크 해제");
                    }
                });

        });
    });


    // 페이지 로드 시 localStorage에서 텍스트를 복원
    const savedSortText = localStorage.getItem('sortButtonText');
    if (savedSortText) {
        document.getElementById('sortButtonText').innerText = savedSortText;
    }

});

// 댓글 정렬 시 드롭다운 메뉴 버튼 클릭 시, 정렬 text 변환 js
function setSortButtonText(text) {
    document.getElementById('sortButtonText').innerText = text;
    localStorage.setItem('sortButtonText', text);
}
