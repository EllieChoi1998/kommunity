document.addEventListener('DOMContentLoaded', (event) => {
    const deleteElements = document.querySelectorAll(".delete");
    const likeElements = document.querySelectorAll(".like");
    const dislikeElements = document.querySelectorAll(".dislike");

    // CSRF 토큰 설정
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

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
                        window.location.reload();
                    } else {
                        alert("삭제 실패");
                    }
                });
            }
        });
    });

    // 댓글 좋아요 처리
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

    // 댓글 비추천 처리
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
});
