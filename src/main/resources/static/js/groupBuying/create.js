document.addEventListener('DOMContentLoaded', function () {
    document.querySelector('.fa-image').onclick = function () {
        document.getElementById('file-upload').click();
    };
});

function validateForm() {
    var title = document.getElementById("title").value;
    var category = document.getElementById("category").value;
    var product = document.getElementById("product").value;
    var price = document.getElementById("price").value;
    var deadline = document.getElementById("deadline").value;
    var chatLink = document.getElementById("chatLink").value;
    var meetingPlace = document.getElementById("meetingPlace").value;
    var content = document.getElementById("content").value;

    if (title === "" || category === "" || product === "" || price === "" || deadline === "" || chatLink === "" || meetingPlace === "" || content === "") {
        alert("모든 필수 입력란을 작성해주세요.");
        return false;
    }
    return true;
}