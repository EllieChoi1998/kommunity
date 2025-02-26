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

        });
    });

    // 좋아요 처리
    likeElements.forEach(element => {
        element.addEventListener('click', function () {
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

        });
    });

    // 비추천 처리
    dislikeElements.forEach(element => {
        element.addEventListener('click', function () {
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

        });
    });


    // 북마크 처리
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
                        window.location.reload();
                    }
                });

        });
    });

    // 페이지 로드 시 항상 기본값으로 "댓글 정렬"로 표시
    const sortButtonTextElement = document.getElementById('sortButtonText');
    const defaultSortText = '댓글 정렬';
    sortButtonTextElement.innerText = defaultSortText;
    localStorage.setItem('sortButtonText', defaultSortText);
});

