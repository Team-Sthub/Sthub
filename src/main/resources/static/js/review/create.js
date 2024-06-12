function validateCreateReviewForm() {
    // 여기에서 폼 유효성 검사를 구현합니다.
    return true; // 유효성 검사 결과에 따라 true 또는 false를 반환합니다.
}

function submitReviewForm(event) {
    event.preventDefault();

    if (!validateCreateReviewForm()) {
        return false;
    }

    const selectedTags = document.querySelectorAll('.tag-container .tag-box');
    if (selectedTags.length === 0) {
        alert('태그를 하나 이상 선택해주세요.');
        return false;
    }
    const tags = {
        tag1: 0,
        tag2: 0,
        tag3: 0,
        tag4: 0,
        tag5: 0,
        tag6: 0
    };

    selectedTags.forEach(tag => {
        const tagValue = tag.getAttribute('data-tag');
        tags[tagValue] = 1; // 선택된 태그는 1로 설정
    });

    // purchaseId를 가져옴
    const purchaseId = document.querySelector('input[name="purchaseId"]').value;
    const rating = document.querySelector('input[name="rating"]:checked')?.value || 0;

    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/review/create';

    const reviewDTO = {
        purchaseId: parseInt(purchaseId),
        rating: parseFloat(rating),
        ...tags
    };

    for (const key in reviewDTO) {
        if (reviewDTO.hasOwnProperty(key)) {
            const hiddenField = document.createElement('input');
            hiddenField.type = 'hidden';
            hiddenField.name = key;
            hiddenField.value = reviewDTO[key];
            form.appendChild(hiddenField);
        }
    }

    document.body.appendChild(form);
    form.submit();
}

function toggleTag(event) {
    const tag = event.target;
    if (tag.classList.contains('tag-box')) {
        tag.classList.remove('tag-box');
        tag.classList.add('tag-box-non');
    } else {
        tag.classList.remove('tag-box-non');
        tag.classList.add('tag-box');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const tags = document.querySelectorAll('.tag-container span');
    tags.forEach(tag => tag.addEventListener('click', toggleTag));
});
