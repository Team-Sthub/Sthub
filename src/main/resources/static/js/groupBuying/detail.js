document.addEventListener('DOMContentLoaded', function () {
    var categoryElement = document.getElementById('category');
    var categoryValue = categoryElement.textContent;

    var categoryMap = {
        'BOOK': '교재',
        'ELECTRONIC': '전자기기',
        'STATIONERY': '문구',
        'TRANSFER': '양도'
    };

    if (categoryMap[categoryValue]) {
        categoryElement.textContent = categoryMap[categoryValue];
    }
});

document.addEventListener('DOMContentLoaded', (event) => {
    const form = document.getElementById('update');
    const groupBuyingId = document.getElementById('groupBuyingId').value;
    const statusElement = document.getElementById('status');

    // 기존 상태 값을 저장
    let originalStatus = statusElement.innerText.trim();

    if (statusElement) { // 요소가 존재하는지 확인
        statusElement.addEventListener('click', () => {
            // 상태 변경
            if (statusElement.innerText.trim() === '모집중') {
                statusElement.innerText = '모집완료';
            } else if (statusElement.innerText.trim() === '모집완료') {
                statusElement.innerText = '모집중';
            }
        });
    } else {
        console.log('Status element not found');
    }

    const updateBtn = document.getElementById('updateBtn');
    updateBtn.addEventListener('click', (event) => {
        event.preventDefault(); // 기본 이벤트 제거 (페이지 새로고침 방지)

        const formData = new FormData();
        var requestData = {
            groupBuyingId : document.getElementById('groupBuyingId').value,
            title: document.getElementById('title').value,
            category : document.getElementById('category').value,
            product : document.getElementById('product').value,
            price: document.getElementById('price').value,
            deadline : document.getElementById('deadline').value,
            chatLink : document.getElementById('chatLink').value,
            meetingPlace : document.getElementById('meetingPlace').value,
            content : document.getElementById('content').value,
            status : statusElement.innerText.trim()
        }

        formData.append('request', new Blob([JSON.stringify(requestData)], { type: 'application/json' }));

        // 파일 업로드 input 요소
        const fileInput = document.getElementById('file-upload');
        // 파일이 있는지 확인

        for (let i = 0; i < fileInput.files.length; i++) {
            formData.append('imgUrl', fileInput.files[i]);
        }


        // fetch 요청
        fetch(`/groupBuying/update?groupBuyingId=${groupBuyingId}`, {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = `/groupBuying/detail?groupBuyingId=${groupBuyingId}`;

                }else{
                    throw new Error('수정 실패');
                }
            }).catch(error => {
                console.error('Error:', error);
                alert('수정 실패');
            });
    });
});
