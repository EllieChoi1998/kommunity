const hashtagsInput = document.getElementById("hashtags");
const hashtagsContainer = document.getElementById("hashtags-container");
const hiddenHashtagsInput = document.getElementById("hashtags-hidden");

let hashtags = [];

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

hashtagsInput.addEventListener("keydown", (event) => {
    if (event.key === 'Enter') {
        event.preventDefault();
        const tag = hashtagsInput.value.trim();
        if (tag) {
            addHashtag(tag);
            hashtagsInput.value = "";
        }
    }
});

$(document).ready(function() {
    var hashtagsList = /*[[${hashtagDTO}]]*/ [];

    $("#hashtags").on("input", function() {
        let query = $(this).val().toLowerCase();
        let searchResults = $("#search-results");
        searchResults.empty();
        if (query.length > 0) {
            let filteredHashtags = hashtagsList.filter(function(hashtag) {
                return hashtag.name.toLowerCase().includes(query);
            });
            filteredHashtags.forEach(function(hashtag) {
                searchResults.append(
                    `<div class="search-result-item" data-name="${hashtag.name}">${hashtag.name}</div>`
                );
            });
        }
    });

    $(document).on("click", ".search-result-item", function() {
        let selectedHashtag = $(this).data("name");
        $("#hashtags").val(selectedHashtag);
        $("#search-results").empty();
    });
});
