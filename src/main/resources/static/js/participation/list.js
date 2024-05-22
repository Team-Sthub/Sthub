function handleAcceptance(acceptValue, participationId, memberId) {
    fetch('/list?memberId=${memberId}&participationId=${participationId}', {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'memberId': memberId
        },
        body: JSON.stringify({
            participationId: participationId,
            accept: acceptValue
        })
    });
}