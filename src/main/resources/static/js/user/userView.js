// 마우스를 올렸을 때 배경색을 변경하는 함수 정의
function changeBackgroundColorOnHover(event) {
    // 모든 메뉴의 배경색 초기화
    document.querySelectorAll('.menu').forEach(function(item) {
        item.style.backgroundColor = ''; // 기본값으로 설정하여 원래 색상으로 돌아오게 함
    });
    // 해당 메뉴의 배경색 변경
    event.target.style.backgroundColor = 'rgba(76, 175, 80, 0.15)';
}

// 마우스를 벗어났을 때 원래 색상으로 돌아오는 함수 정의
function changeBackgroundColorOnMouseLeave(event) {
    // 해당 메뉴의 배경색 초기화
    event.target.style.backgroundColor = ''; // 기본값으로 설정하여 원래 색상으로 돌아오게 함
}

// 클릭했을 때 배경색을 변경하는 함수 정의
function changeBackgroundColorOnClick(event) {
    event.target.style.backgroundColor = 'rgba(76, 175, 80, 0.15)';
    var href = event.target.getAttribute('data-href');
    window.location.href = href;
}

// 각 메뉴 요소에 마우스를 올렸을 때, 벗어났을 때, 클릭했을 때 이벤트 추가
var menuItems = document.querySelectorAll('.menu');
menuItems.forEach(function(item) {
    item.addEventListener('mouseenter', changeBackgroundColorOnHover);
    item.addEventListener('mouseleave', changeBackgroundColorOnMouseLeave);
    item.addEventListener('click', changeBackgroundColorOnClick);
});

// 각 메뉴 클릭 시 해당 화면 동적 로드
$(document).ready(function() {
    $('.menu').click(function() {
        var target = $(this).data('target');
        $('#categoryContent').load('/load-' + target);
    });
});
