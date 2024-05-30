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

function validateCreateForm() {
    var title = document.getElementById("sub-title").value;
    var category = document.getElementById("category").value;
    var product = document.getElementById("product").value;
    var price = document.getElementById("price").value;

    if (title === "" || category === "" || product === "" || price === "") {
        alert("모든 필수 입력란을 작성해주세요.");
        return false;
    }
    return true;
}