function validateCreateComplaintForm(event) {
    const checkbox = document.getElementById('my_checkbox');
    if (!checkbox.checked) {
        alert('체크박스를 선택해주세요.');
        event.preventDefault(); // 폼 제출을 막습니다.
        return false;
    }
    return true; // 유효성 검사 결과에 따라 true 또는 false를 반환합니다.
}

function submitComplaintForm(event) {
    event.preventDefault();

    if (!validateCreateComplaintForm(event)) {
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
        tag6: 0,
        tag7: 0,
        tag8: 0,
        tag9: 0
    };

    selectedTags.forEach(tag => {
        const tagValue = tag.getAttribute('data-tag');
        tags[tagValue] = 1; // 선택된 태그는 1로 설정
    });

    const form = document.querySelector('.complaint-form');

    const complaintDTO = {
        ...tags
    };

    for (const key in complaintDTO) {
        if (complaintDTO.hasOwnProperty(key)) {
            const hiddenField = document.createElement('input');
            hiddenField.type = 'hidden';
            hiddenField.name = key;
            hiddenField.value = complaintDTO[key];
            form.appendChild(hiddenField);
        }
    }

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
