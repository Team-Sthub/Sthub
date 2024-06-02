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

document.addEventListener('DOMContentLoaded', function() {
    // 구매 관련
    const cardNum1 = document.querySelector('.cardNum1');
    const cardNum2 = document.querySelector('.cardNum2');
    const cardNum3 = document.querySelector('.cardNum3');
    const cardNum4 = document.querySelector('.cardNum4');
    const cardExp_year = document.querySelector('.cardExp_year');
    const cardExp_month = document.querySelector('.cardExp_month');
    const cardPassword = document.querySelector('.cardPassword');
    const cvcNum = document.querySelector('.cvcNum');

    const purchase = document.querySelector('.purchase');
    const close1 = document.querySelector('.close1');
    const submit_purchase = document.getElementById('submit_purchase');

    // 구매하기 버튼 클릭 시
    purchase.addEventListener('click', function() {
        // display 속성을 block로 변경
        document.querySelector('.modal1').style.display = 'block';
    });

    // 구매 닫기 버튼 클릭 시
    close1.addEventListener('click', function() {
        // display 속성을 none로 변경
        document.querySelector('.modal1').style.display = 'none';
    });

    // 구매 전송 버튼 클릭 시
    submit_purchase.addEventListener('click', function() {
        // 모든 필드 다 입력했는지 검사
        if(cardNum1.value === "" || cardNum2.value === "" || cardNum3.value === "" || cardNum4.value === "" ||
            cardExp_month.value === "" || cardExp_year.value === "" || cardPassword.value === "" || cvcNum.value === "") {
            console.log("cardNum1 : " + cardNum1.value);
            alert("모든 값을 입력해주세요.")
            return false;
        }

        const secondhandId = document.querySelector('#secondhandId').value;
        fetch("/purchase?secondhandId=" + secondhandId, {
            method: 'POST',
        }).then(response => {
            if (response.ok) {
                alert('구매에 성공했습니다.');
                window.location.href = `/secondhand/detail?secondhandId=` + secondhandId;
            } else {
                alert('구매 중 오류가 발생했습니다.');
            }
        }).catch(error => {
            console.error('Error:', error);
            alert('구매 중 오류가 발생했습니다.');
        });
    });
});
