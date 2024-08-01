const hashtagsInput = document.getElementById("hashtags");
const hashtagsContainer = document.getElementById("hashtags-container");
const hiddenHashtagsInput = document.getElementById("hashtags-hidden");
const searchResults = document.getElementById("search-results");

let hashtags = [];
let hashtagDTOs = []; // 한 번만 선언
let highlightedIndex = -1; // 현재 강조된 항목의 인덱스

// Axios를 사용하여 데이터 가져오기
axios.get('/api/hashtags')
    .then(response => {
        hashtagDTOs = response.data;
        console.log('HashtagDTOs loaded:', hashtagDTOs); // 데이터 확인
    })
    .catch(error => {
        console.error('Error fetching hashtagDTOs:', error);
    });

function addHashtag(tag) {
    tag = tag.replace(/[\[\]]/g, '').trim();
    if (tag && !hashtags.includes(tag)) {
        const span = document.createElement("span");
        span.innerText = "#" + tag + " ";
        span.classList.add("hashtag");

        const removeButton = document.createElement("button");
        removeButton.innerText = "x";
        removeButton.classList.add("remove-button");
        removeButton.addEventListener("click", () => {
            hashtagsContainer.removeChild(span);
            hashtags = hashtags.filter((hashtag) => hashtag !== tag);
            hiddenHashtagsInput.value = hashtags.join(",");
        });

        span.appendChild(removeButton);
        hashtagsContainer.appendChild(span);
        hashtags.push(tag);
        hiddenHashtagsInput.value = hashtags.join(",");
    }
}

function displaySearchResults(results) {
    searchResults.innerHTML = ''; // 기존 결과를 초기화
    results.forEach((hashtag, index) => {
        const div = document.createElement("div");
        div.innerText = `#${hashtag}`;
        div.classList.add("search-result-item");
        div.dataset.index = index; // 인덱스를 데이터 속성에 저장

        div.addEventListener("click", () => {
            addHashtag(hashtag);
            searchResults.innerHTML = ''; // 해시태그 추가 후 검색 결과 초기화
            hashtagsInput.value = ""; // 입력 필드 초기화
            hashtagsInput.focus(); // 입력 필드 포커스
        });

        searchResults.appendChild(div);
    });

    // 초기 강조 인덱스 설정
    highlightedIndex = -1;
}

function navigateResults(direction) {
    const items = searchResults.querySelectorAll('.search-result-item');
    if (items.length === 0) return;

    // Remove highlight from previous item
    if (highlightedIndex >= 0 && highlightedIndex < items.length) {
        items[highlightedIndex].classList.remove('highlighted');
    }

    if (direction === 'down') {
        highlightedIndex = (highlightedIndex + 1) % items.length;
    } else if (direction === 'up') {
        highlightedIndex = (highlightedIndex - 1 + items.length) % items.length;
    }

    // Highlight new item
    if (highlightedIndex >= 0 && highlightedIndex < items.length) {
        items[highlightedIndex].classList.add('highlighted');
        items[highlightedIndex].scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }
}

hashtagsInput.addEventListener("input", () => {
    const query = hashtagsInput.value.trim().toLowerCase();
    if (query.length > 0 && hashtagDTOs.length > 0) {
        const filteredHashtags = hashtagDTOs.filter(hashtag =>
            hashtag.toLowerCase().includes(query)
        );
        displaySearchResults(filteredHashtags);
    } else {
        searchResults.innerHTML = ''; // 검색어가 없거나 데이터가 없으면 결과를 초기화
    }
});

hashtagsInput.addEventListener("keydown", (event) => {
    if (event.key === 'Enter') {
        event.preventDefault();
        const items = searchResults.querySelectorAll('.search-result-item');
        if (highlightedIndex >= 0 && highlightedIndex < items.length) {
            const selectedHashtag = items[highlightedIndex].innerText.substring(1);
            addHashtag(selectedHashtag);
            searchResults.innerHTML = ''; // 해시태그 추가 후 검색 결과 초기화
            hashtagsInput.value = "";
        }
    } else if (event.key === 'ArrowDown') {
        event.preventDefault();
        navigateResults('down');
    } else if (event.key === 'ArrowUp') {
        event.preventDefault();
        navigateResults('up');
    }
});

document.getElementById('search-btn').addEventListener('click', () => {
    const searchHashtags = hiddenHashtagsInput.value.split(',').filter(tag => tag.trim() !== '');
    const boardId = document.getElementById('boardId').value;
    if (searchHashtags.length > 0) {

        const queryParams = new URLSearchParams({
            hashtags: searchHashtags.join(','),
        });

        window.location.href = `/posts/search/${boardId}?${queryParams.toString()}`;
    } else {
        alert("Please enter at least one hashtag and select a board.");
    }
});

