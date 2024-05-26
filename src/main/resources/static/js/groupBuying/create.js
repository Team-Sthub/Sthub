function validateForm() {
    var title = document.getElementById("sub-title").value;
    var category = document.getElementById("category").value;
    var product = document.getElementById("product").value;
    var price = document.getElementById("price").value;
    var deadline = document.getElementById("deadline").value;
    var chatLink = document.getElementById("chatLink").value;
    var place = document.getElementById("place").value;

    if (title === "" || category === "" || product === "" || price === "" || deadline == "" || chatLink == "" || place == "") {
        alert("모든 필수 입력란을 작성해주세요.");
        return false;
    }
    return true;
}