function handleAcceptance(acceptStatus, participationId, memberId, groupBuyingId) {
    const request = {
        groupBuyingId: groupBuyingId,
        participationId: participationId,
        accept: acceptStatus
    };

    fetch(`/participation/list`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(request)
    })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                alert("수락/거절 처리에 실패했습니다.");
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("수락/거절 처리 중 오류가 발생했습니다.");
        });
}
