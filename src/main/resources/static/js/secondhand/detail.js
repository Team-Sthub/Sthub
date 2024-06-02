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

document.addEventListener('DOMContentLoaded', function() {
    const btnDelivery = document.getElementById('btn_delivery');
    const btnDirect = document.getElementById('btn_direct');
    const transactionInfo = document.querySelector('.transaction_info');
    const typeInfo = document.querySelector('.type_info');
    const close = document.querySelector('.close');
    const submit = document.getElementById('submit');
    var type = null;

    // 택배 버튼 클릭 시
    btnDelivery.addEventListener('click', function() {
        btnDelivery.style.backgroundColor = 'white';
        btnDirect.style.backgroundColor = '';
        transactionInfo.textContent = '운송장번호';
        typeInfo.placeholder = "'-'를 제외한 숫자만 입력해주세요.";
        type = "DELIVERY";
    });

    // 직거래 버튼 클릭 시
    btnDirect.addEventListener('click', function() {
        btnDirect.style.backgroundColor = 'white';
        btnDelivery.style.backgroundColor = '';
        transactionInfo.textContent = '장소';
        typeInfo.placeholder = "예시) 서울시 성북구 화랑로13길 60";
        type = "DIRECT";
    });

    // 닫기 버튼 클릭 시
    close.addEventListener('click', function() {
        // display 속성을 none로 변경
        document.querySelector('.modal').style.display = 'none';
    });

    // 전송 버튼 클릭 시
    submit.addEventListener('click', function() {
        if(type === "" || typeInfo.value === "") {
            alert("모든 값을 입력해주세요.");
            return false;
        }

        const secondhandId = document.querySelector('#secondhandId').value;
        // JSON 데이터를 FormData에 추가
        var formData = {
            secondhandId: secondhandId,
            type: type,
            typeInfo: typeInfo.value
        };

        console.log(formData);
        fetch("/secondhand/check", {
            method: 'POST',
            body: JSON.stringify(formData),
            headers: {
                'Content-type' : 'application/json'
            }
        }).then(response => {
            if (response.ok) {
                window.location.href = `/secondhand/detail?secondhandId=` + secondhandId;
            } else {
                alert('수정 중 오류가 발생했습니다.');
            }
        }).catch(error => {
            console.error('Error:', error);
            alert('수정 중 오류가 발생했습니다.');
        });
    });
});
