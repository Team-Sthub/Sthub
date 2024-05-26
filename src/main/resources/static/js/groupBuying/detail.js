document.addEventListener('DOMContentLoaded', function () {
    var categoryElement = document.getElementById('category');
    var categoryValue = categoryElement.textContent;

    var categoryMap = {
        'BOOK': '교재',
        'ELECTRONIC': '전자기기',
        'STATIONERY': '문구',
        'TRANSFER': '양도'
    };

    if (categoryMap[categoryValue]) {
        categoryElement.textContent = categoryMap[categoryValue];
    }
});