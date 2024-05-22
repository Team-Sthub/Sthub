// 지도 띄우기
let map;
var address;
var file;

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
                        address = results[0].formatted_address.split(" ").slice(1, 4).join(" ");
                        displayAddress(address); // 결과를 화면에 출력
                    } else {
                        displayAddress('주소를 찾을 수 없습니다.');
                    }
                } else {
                    displayAddress('Geocoder 오류: ' + status);
                }
            });

            // 현재 위치 화면에 출력
            function displayAddress(address) {
                var displayDiv = document.getElementById('address-display');
                displayDiv.innerText = address;
            }
        }, function() {
            handleLocationError(true, infoWindow, map.getCenter());
        });
    } else {
        // 브라우저가 Geolocation을 지원하지 않는 경우
        handleLocationError(false, infoWindow, map.getCenter());
    }
}

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

// 주소로 위치 찾기
function searchAddress() {
    const inputAddress = document.getElementById('address-input').value;
    console.error("검색창에 들어온 주소는 " + inputAddress);

    geocoder.geocode({'address': inputAddress}, function(results, status) {
        if (status === 'OK') {
            const pos = results[0].geometry.location;
            map.setCenter(pos);

            if (marker) {
                marker.setMap(null);
            }
            marker = new google.maps.Marker({
                position: pos,
                map: map,
                title: inputAddress
            });

            displayAddress(results[0].formatted_address);
            document.getElementById('latitude').value = pos.lat();
            document.getElementById('longitude').value = pos.lng();
        } else {
            console.error('Geocode was not successful for the following reason: ' + status);
        }
    });
}

// 이미지 미리보기
document.addEventListener('DOMContentLoaded', function() {
    const imageIcon = document.getElementById('image-icon');
    const fileInput = document.getElementById('file-input');
    const profileField = document.getElementById('profile');

    if (imageIcon && fileInput && profileField) {
        imageIcon.addEventListener('click', function() {
            fileInput.click();
        });

        fileInput.addEventListener('change', function(event) {
            file = event.target.files[0];
            if (file) {
                profileField.value = file.name;
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

// 위치 정보를 가져와 폼 필드에 설정한 후 폼을 제출
function getLocationAndSubmit() {
    const addressInput = document.getElementById('address-input');
    const addressField = document.getElementById('address');

    if (addressInput.value !== "") {
        addressField.value = addressInput.value;
    } else {
        addressField.value = address;
    }
    
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            document.getElementById('latitude').value = position.coords.latitude;
            document.getElementById('longitude').value = position.coords.longitude;
            submitForm();
        }, function(error) {
            alert("Geolocation is not supported by this browser or an error occurred.");
            submitForm();
        });
    } else {
        alert("Geolocation is not supported by this browser.");
        submitForm();
    }
}

// 위치 정보를 가져오는데 실패한 경우 오류를 처리
function handleLocationError(browserHasGeolocation, pos) {
    alert(browserHasGeolocation ?
        'Error: The Geolocation service failed.' :
        'Error: Your browser doesn\'t support geolocation.');
}

// 폼 데이터 서버 전송
function submitForm() {
    const form = document.getElementById('register-form');
    const formData = new FormData(form);

    const fileInput = document.getElementById('file-input');
    if (fileInput.files.length > 0) {
        formData.append('profile', fileInput.files[0]);
    }

    fetch(form.action, {
        method: 'POST',
        body: formData,
    }).then(response => {
        return response.text(); // 서버로부터의 응답을 처리
    }).then(text => {
        console.log(text); // 서버 응답 출력
    }).catch(error => {
        console.error('Error:', error);
    });
}