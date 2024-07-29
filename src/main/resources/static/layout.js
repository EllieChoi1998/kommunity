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
        if (!$(event.target).closest('#profileMenuContainer, #profileBtn').length) {
            $('#profileMenuContainer').addClass('hidden').removeClass('visible');
        }
    });
});