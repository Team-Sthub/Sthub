document.addEventListener('DOMContentLoaded', (event) => {
    window.deleteGroupBuying = function(groupBuyingId) {
        if (!confirm('정말로 삭제하시겠습니까?')) {
            return; // 사용자가 삭제를 취소하면 함수 종료
        }

        fetch(`/delete?groupBuyingId=${groupBuyingId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('삭제 실패');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('삭제 실패');
            });
    };
});
