function toggleTransaction(type) {
    const delivery = document.getElementById('delivery');
    const direct = document.getElementById('direct');
    const hiddenInput = document.getElementById('type');

    if (type === 'DELIVERY') {
        if (delivery.style.color === 'black') {
            delivery.style.color = 'gray';
            delivery.classList.remove('selected');
        } else {
            delivery.style.color = 'black';
            delivery.classList.add('selected');
        }
    } else if (type === 'DIRECT') {
        if (direct.style.color === 'black') {
            direct.style.color = 'gray';
            direct.classList.remove('selected');
        } else {
            direct.style.color = 'black';
            direct.classList.add('selected');
        }
    }

    if (delivery.classList.contains('selected') && direct.classList.contains('selected')) {
        hiddenInput.value = 'ALL';
    } else if (delivery.classList.contains('selected')) {
        hiddenInput.value = 'DELIVERY';
    } else if (direct.classList.contains('selected')) {
        hiddenInput.value = 'DIRECT';
    } else {
        hiddenInput.value = '';
    }
}

function sendDeleteRequest(secondhandId) {
    alert(secondhandId);
    var url = '/secondhand/delete?secondhandId=' + secondhandId;

    fetch(url, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                // 요청이 성공하면 원하는 작업 수행 (예: 페이지 리다이렉트)
                window.location.href = '/secondhand/list/ALL?pageNum=1';
            } else {
                // 요청이 실패하면 에러 처리
                alert('삭제에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('삭제 중 오류가 발생했습니다.');
        });
}

// 거래 최종 방식 클릭 시 동작
function check() {
    // display 속성을 block로 변경
    document.querySelector('.modal').style.display = 'block';
}

// 모달창 닫기 클릭 시 동작
function close() {
    // display 속성을 none로 변경
    document.querySelector('.modal').style.display = 'none';
}