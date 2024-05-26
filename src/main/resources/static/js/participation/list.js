// // function handleAcceptance(acceptValue, participationId, memberId) {
// //     fetch('/list?memberId=${memberId}&participationId=${participationId}', {
// //         method: 'PATCH',
// //         headers: {
// //             'Content-Type': 'application/json',
// //             'memberId': memberId
// //         },
// //         body: JSON.stringify({
// //             participationId: participationId,
// //             accept: acceptValue
// //         })
// //     });
// // }
//
// function handleAcceptance(status, participationId, memberId, groupBuyingId) {
//     const request = {
//         groupBuyingId: groupBuyingId,
//         accept: status
//     };
//
//     fetch(`/list?participationId=${participationId}`, {
//         method: 'PATCH',
//         headers: {
//             'Content-Type': 'application/json',
//             'X-CSRF-TOKEN': /* CSRF 토큰을 여기에 넣으세요 */
//         },
//         body: JSON.stringify(request)
//     })
//         .then(response => response.json())
//         .then(data => {
//             if (data.success) {
//                 location.reload(); // 성공 시 페이지 새로 고침
//             } else {
//                 alert('오류가 발생했습니다.');
//             }
//         })
//         .catch(error => console.error('Error:', error));
// }