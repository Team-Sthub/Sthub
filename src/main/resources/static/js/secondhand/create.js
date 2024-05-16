document.querySelector('.fa-image').onclick = function() {
    document.getElementById('file-upload').click();
};

function toggleColor(elementId) {
    var element = document.getElementById(elementId);
    if (element.style.color === 'gray') {
        element.style.color = 'black'; // 클릭하면 검정색으로 변경
    } else {
        element.style.color = 'gray'; // 다시 클릭하면 회색으로 변경
    }
}