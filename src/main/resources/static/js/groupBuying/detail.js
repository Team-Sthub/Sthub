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

        const groupBuyingId = document.getElementById('groupBuyingId').value;
        const title = document.getElementById("title").value;
        const category = document.getElementById("category").value;
        const product = document.getElementById("product").value;
        const price = document.getElementById("price").value;
        const deadline = document.getElementById('deadline').value;
        const chatLink = document.getElementById('chatLink').value;
        const meetingPlace = document.getElementById('meetingPlace').value;
        const content = document.querySelector("textarea[name='content']").value;

        const formData = new FormData();
        var requestData = {
            groupBuyingId : groupBuyingId,
            title: title,
            category : category,
            product : product,
            price: price,
            deadline : deadline,
            chatLink : chatLink,
            meetingPlace : meetingPlace,
            content : content,
            status : statusElement.innerText.trim(),
            deleteImages: deleteImages
        }

        if (title === "" || category === "" || product === "" || price === "" || deadline == "" || chatLink == "" || meetingPlace == "" || content == "") {
            alert("모든 필수 입력란을 작성해주세요.");
            return false;
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
                    window.location.href = `/groupBuying/${groupBuyingId}`;
                }else{
                    throw new Error('수정 실패');
                }
            }).catch(error => {
                console.error('Error:', error);
                alert('수정 실패');
            });
    });
});

var deleteImages = [];

function removeImage(button) {
    var imgPath = button.getAttribute('data-img-path');
    // 이미지 제거 로직 수행 (DOM에서 이미지 제거)
    var imageInfoDiv = button.closest('.image-info');
    imageInfoDiv.remove();

    // 이미지 삭제 버튼에 data-img-path 속성이 있는 경우에만 deleteImages 배열에 추가
    if (imgPath) {
        deleteImages.push(imgPath);
    }

    // 필요하다면 imgPath 값을 활용하여 서버에 삭제 요청 등 수행
    console.log('Image to be removed:', imgPath);
    console.log(deleteImages);
}