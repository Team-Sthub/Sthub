document.querySelector('.fa-image').onclick = function() {
    document.getElementById('file-upload').click();
};

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

document.getElementById('update-form').addEventListener('submit', validateUpdateForm);
function validateUpdateForm(event) {
    event.preventDefault();

    var form = document.getElementById("update-form")
    var formData = new FormData(); // 폼 데이터 객체 생성
    const title = document.getElementById("title").value;
    const category = document.getElementById("category").value;
    const product = document.getElementById("product").value;
    const price = document.getElementById("price").value;
    const place = document.getElementById("place").value;
    const secondhandId = document.getElementById("secondhandId").value;
    const content = document.querySelector("textarea[name='content']").value;
    const delivery = document.getElementById('delivery');
    const direct = document.getElementById('direct');
    var type = document.getElementById('type');

    if (delivery.classList.contains('selected') && direct.classList.contains('selected')) {
        type.value = 'ALL';
    } else if (delivery.classList.contains('selected')) {
        type.value = 'DELIVERY';
    } else if (direct.classList.contains('selected')) {
        type.value = 'DIRECT';
    } else {
        type.value = '';
    }

    if (title === "" || category === "" || product === "" || price === "" || type === "") {
        alert("모든 필수 입력란을 작성해주세요.");
        return false;
    }

    // JSON 데이터를 FormData에 추가
    var requestData = {
        title: title,
        category: category,
        product: product,
        price: price,
        place: place,
        type: type.value,
        content: content,
        secondhandId: secondhandId,
        deleteImages: deleteImages
    };
    formData.append('request', new Blob([JSON.stringify(requestData)], { type: 'application/json' }));

    // 파일 업로드 input에서 파일들을 formData에 추가
    var fileInput = document.getElementById('file-upload');
    for (var i = 0; i < fileInput.files.length; i++) {
        formData.append('imgUrl', fileInput.files[i]);
    }

    fetch(form.action, {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            window.location.href = `/secondhand/detail?secondhandId=${secondhandId}`;
        } else {
            alert('수정 중 오류가 발생했습니다.');
        }
    }).catch(error => {
        console.error('Error:', error);
        alert('수정 중 오류가 발생했습니다.');
    });
}
