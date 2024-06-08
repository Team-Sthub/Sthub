document.addEventListener('DOMContentLoaded', function() {
    const errorMessageElement = document.getElementById('errorMessage');

    // 서버에서 전달한 에러 메시지가 존재하면 alert 창으로 표시
    if (errorMessage) {
        alert("게시물이 존재하지 않습니다.");
    }
});