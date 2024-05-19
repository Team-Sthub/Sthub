// 지도 띄우기
let map;

function initMap() {
    // 지도 생성
    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: -34.397, lng: 150.644}, // 초기 중심 위치 (예시)
        zoom: 12.3 // 초기 줌 레벨
    });

    // 현재 위치 가져오기
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var pos = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };

            var marker = new google.maps.Marker({
                position: pos,
                map: map,
                title: '현재 위치'
            });

            // 현재 위치를 지도 중심으로 설정
            map.setCenter(pos);

            // 위도와 경도를 주소로 변환하여 출력
            var geocoder = new google.maps.Geocoder;
            geocoder.geocode({'location': pos}, function(results, status) {
                if (status === 'OK') {
                    if (results[0]) {
                        var address = results[0].formatted_address.split(" ").slice(1, 4).join(" ");
                        displayAddress(address); // 결과를 화면에 출력
                    } else {
                        displayAddress('주소를 찾을 수 없습니다.');
                    }
                } else {
                    displayAddress('Geocoder 오류: ' + status);
                }
            });
        }, function() {
            handleLocationError(true, infoWindow, map.getCenter());
        });
    } else {
        // 브라우저가 Geolocation을 지원하지 않는 경우
        handleLocationError(false, infoWindow, map.getCenter());
    }
}

// 주소 검색 추후.....
/*function geocodeAddress(address) {
    geocoder.geocode({ 'address': address }, function (results, status) {
        results += "대한민국";
        if (status === 'OK') {
            map.setCenter(results[0].geometry.location);
            new google.maps.Marker({
                map: map,
                position: results[0].geometry.location
            });
        } else {
            displayAddress('Geocode was not successful for the following reason: ' + status);
        }
    });
}

function displayAddress(address) {
    document.getElementById('address-display').innerText = address;
}

function handleLocationError(browserHasGeolocation, pos) {
    var errorMsg = browserHasGeolocation ? 'Error: The Geolocation service failed.' : 'Error: Your browser doesn\'t support geolocation.';
    alert(errorMsg);
}

document.getElementById('search-button').addEventListener('click', function () {
    var address = document.getElementById('address').value;
    geocodeAddress(address);
});*/

// 이미지 업로드
document.addEventListener('DOMContentLoaded', function() {
    const imageIcon = document.getElementById('image-icon');
    const fileInput = document.getElementById('file-input');

    if (imageIcon && fileInput) {
        imageIcon.addEventListener('click', function() {
            fileInput.click();
        });

        fileInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    imageIcon.style.backgroundImage = `url(${e.target.result})`;
                    imageIcon.style.backgroundSize = 'cover';
                    imageIcon.style.backgroundPosition = 'center';
                    imageIcon.style.color = 'transparent'; // 아이콘 텍스트를 숨김
                };
                reader.readAsDataURL(file);
            }
        });
    } else {
        console.error("Image icon or file input element not found");
    }
});



// 중복 확인 버튼을 누를 때의 동작 설정
document.getElementById("checkId").addEventListener("click", function() {
    // AJAX를 이용해 GET 요청 보냄
    let xhr = new XMLHttpRequest();
    let nickname = document.getElementById("nickname").value;
    xhr.open("GET", "/user/register/check?nickname=" + encodeURIComponent(nickname), true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // 응답 처리
            alert(xhr.responseText); // 서버에서 받은 응답을 그대로 출력
        }
    };
    xhr.send();
});
