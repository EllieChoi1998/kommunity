$(document).ready(function() {
    console.log('Document is ready - layout.js loaded');

    let autoLogoutDisabled = false;

    function checkAutoLogout() {
        const now = new Date();
        const hours = now.getHours();
        const minutes = now.getMinutes();

        // 6시가 된 경우 자동 로그아웃
        if (!autoLogoutDisabled && hours === 15 && minutes === 0) {
            alert('오후 6시가 되어 자동 로그아웃됩니다.');
            fetch('/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(() => {
                window.location.href = '/login'; // 로그아웃 후 로그인 페이지로 리디렉션
            });
        }
    }

    function disableAutoLogoutIfAfterSix() {
        const now = new Date();
        const hours = now.getHours();

        // 현재 시간이 6시 이후면 자동 로그아웃 비활성화
        if (hours >= 15) {
            autoLogoutDisabled = true;
        }
    }

    // 페이지 로드 시 시간 확인하여 자동 로그아웃 비활성화 설정
    disableAutoLogoutIfAfterSix();

    // 매 1분마다 시간 체크하여 6시가 되면 로그아웃
    setInterval(checkAutoLogout, 60000);

    // 페이지 로드 시 한 번 체크
    checkAutoLogout();

    $('#pills-tab a').on('click', function(e) {
        e.preventDefault();
        var url = $(this).attr('href');

        // 모든 nav-link에서 active 클래스 제거
        $('#pills-tab a').removeClass('active');

        // 클릭된 nav-link에 active 클래스 추가
        $(this).addClass('active');

        $.get(url, function(data) {
            $('#category-container').html(data);
        });
    });

    // 초기 로드
    $('#pills-tab a.nav-link.active').trigger('click');

    // 닉네임 버튼 클릭 시 프로필 메뉴 로드 및 표시
    $('#profileBtn').on('click', function() {
        $.get('/profile-menu', function(data) {
            $('#profileMenuContainer').html(data).toggleClass('visible').removeClass('hidden');
        });
    });

    // 프로필 메뉴 외부 클릭 시 숨기기
    $(document).click(function(event) {
        if (!$(event.target).closest('#profileMenuContainer, #profileBtn, #mypageButton').length) {
            $('#profileMenuContainer').addClass('hidden').removeClass('visible');
            $('#mypage-menu').hide(); // 마이페이지 메뉴도 숨기기
        }
    });

    // 프로필 관리 버튼 클릭 시 마이페이지 메뉴 로드 및 표시
    $(document).on('click', '#mypageButton', function() {
        $.get('/mypage-menu', function(data) {
            $('#profileMenuContainer').html(data).slideDown(); // 메뉴를 항상 보이도록
        });
    });

    // 보드 클릭 시 카테고리 로드
    $('#pills-tab a[data-board-id]').on('click', function () {
        var boardId = $(this).data('board-id');
        $.get('/categories/' + boardId, function (data) {
            $('#category-container').html(data);
        });
    });
});