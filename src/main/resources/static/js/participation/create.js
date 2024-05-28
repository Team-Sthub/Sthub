function is_checked() {
    const checkbox = document.getElementById('my_checkbox');
}

function validateForm(event) {
    const checkbox = document.getElementById('my_checkbox');
    if (!checkbox.checked) {
        alert('체크박스를 선택해주세요.');
        event.preventDefault(); // 폼 제출을 막습니다.
    }
}

async function handleFormSubmit(participationId, memberId) {
    // 폼 데이터 수집
    const content = document.getElementById('content').value;
    const isConfirmed = document.getElementById('my_checkbox').checked;

    // ParticipationRequestDto.PatchRequest 객체 생성
    const requestData = {
        content: content,
        isConfirmed: isConfirmed
    };

    // POST 요청 보내기
    fetch(`/participation/update?participationId=${participationId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        // ParticipationRequestDto.PatchRequest 객체를 JSON으로 변환하여 본문에 추가
        body: JSON.stringify(requestData)
    })
        .then(response => {
            if (response.ok) {
                alert("수정합니다");
                // 수정 성공 시 올바른 URL로 리디렉션
                window.location.href = `/participation/detail?participationId=${participationId}`;
            } else {
                alert("수정 처리에 실패했습니다.");
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("수정 처리 중 오류가 발생했습니다.");
        });
}
