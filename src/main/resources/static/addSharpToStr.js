document.addEventListener("DOMContentLoaded", function () {
    // strHashtag 요소를 가져옵니다.
    const hashtagInput = document.getElementById("strHashtag");

    // 요소가 존재하면 실행합니다.
    if (hashtagInput) {
        // 기존 해시태그 값을 가져옵니다.
        let hashtags = hashtagInput.value;

        // 해시태그가 비어있지 않으면 처리합니다.
        if (hashtags.trim() !== "") {
            // 각 해시태그 앞에 '#'를 붙여줍니다.
            hashtags = hashtags.split(' ').map(tag => '#' + tag.trim()).join(' ');

            // 변경된 해시태그 값을 입력 필드에 설정합니다.
            hashtagInput.value = hashtags;
        }
    }
});
