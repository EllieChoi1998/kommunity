// sort.js
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('sortSelect').onchange = function() {
        const sortValue = this.value;
        const urlParams = new URLSearchParams(window.location.search);
        urlParams.set('sort', sortValue);
        window.location.search = urlParams.toString();
    };
});
