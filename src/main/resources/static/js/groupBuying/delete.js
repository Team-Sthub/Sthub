document.addEventListener('DOMContentLoaded', (event) => {
    window.deleteGroupBuying = function(groupBuyingId) {
        if (!confirm('정말로 삭제하시겠습니까?')) {
            return; // 사용자가 삭제를 취소하면 함수 종료
        }

        fetch(`/groupBuying?groupBuyingId=${groupBuyingId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = '/groupBuying/list/ALL?pageNum=1';
                } else {
                    throw new Error('삭제 실패');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('삭제 실패');
            });
    };
});
