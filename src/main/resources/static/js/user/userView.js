// 마우스를 올렸을 때와 벗어났을 때의 효과를 처리하는 함수 정의
function handleHoverEffects(event) {
    if (event.type === 'mouseenter') {
        event.target.style.backgroundColor = 'rgba(76, 175, 80, 0.15)';
    } else if (event.type === 'mouseleave' && !event.target.classList.contains('selected')) {
        event.target.style.backgroundColor = '';
    }
}

// 메뉴 항목을 클릭했을 때의 동작을 처리하는 함수 정의
function handleMenuItemClick(event) {
    var url = event.target.getAttribute('data-url');

    // URL이 정의되어 있을 때만 AJAX 요청을 보냄
    if (url) {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', url);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    document.getElementById('categoryContent').innerHTML = xhr.responseText;
                } else {
                    console.error('요청 실패: ' + xhr.status);
                }
            }
        };
        xhr.send();
    }

    // 모든 메뉴 항목의 선택 상태 제거
    document.querySelectorAll('.menu').forEach(function(item) {
        item.classList.remove('selected');
        item.style.backgroundColor = '';
    });

    // 클릭한 메뉴 항목에 선택 상태 추가
    event.target.classList.add('selected');
    event.target.style.backgroundColor = 'rgba(76, 175, 80, 0.15)';
}

// 페이지가 로드될 때 실행되는 함수
window.onload = function() {
    // 각 메뉴 항목에 대한 클릭 이벤트 리스너 등록
    var menuItems = document.querySelectorAll('.menu');
    menuItems.forEach(function(item) {
        item.addEventListener('mouseenter', handleHoverEffects);
        item.addEventListener('mouseleave', handleHoverEffects);
        item.addEventListener('click', handleMenuItemClick);
    });

    // 첫 번째 메뉴를 선택하여 해당 내용을 가져옴
    var firstMenuItem = document.querySelector('.menu');
    firstMenuItem.click();
};
