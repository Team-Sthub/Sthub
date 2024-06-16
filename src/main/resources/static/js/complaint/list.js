$(document).ready(function() {
    $('.menu').on('click', function() {
        const url = $(this).data('url');

        $.ajax({
            url: url,
            type: 'GET',
            success: function(response) {
                $('#categoryContent').html(response);
            },
            error: function(error) {
                console.error('Error loading content:', error);
                $('#categoryContent').html('<p>콘텐츠를 로드하는 중 오류가 발생했습니다.</p>');
            }
        });
    });
});
