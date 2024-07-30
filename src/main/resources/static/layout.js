$(document).ready(function() {
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