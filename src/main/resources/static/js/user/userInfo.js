// 저장된 사용자의 위도(latitude)와 경도(longitude)
var map;
var userLatitude; // 사용자의 위도 값
var userLongitude; // 사용자의 경도 값
var marker;
var previousMarker;
var file;

// 지도 초기화 함수
function initMap() {
    userLatitude = parseFloat(document.getElementById("userCoordinates").getAttribute("data-latitude"));
    userLongitude = parseFloat(document.getElementById("userCoordinates").getAttribute("data-longitude"));

    console.log("위도는 " + userLatitude + " 경도는 " + userLongitude);

    // 사용자의 위도와 경도를 기반으로 LatLng 객체 생성
    var userLatLng = new google.maps.LatLng(userLatitude, userLongitude);

    var mapOptions = {
        center: userLatLng, // 지도의 중심을 사용자의 위치로 설정
        zoom: 12.3 // 초기 줌 레벨 설정 (예시로 10으로 설정)
    };

    map = new google.maps.Map(document.getElementById('map'), mapOptions);
    var pos = new google.maps.LatLng(userLatitude, userLongitude);

    marker = new google.maps.Marker({
        position: pos,
        map: map,
        title: '사용자 설정 주소'
    });

    // 사용자 설정 주소 위치를 지도 중심으로 설정
    map.setCenter(pos);

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var pos = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };

        // 위도와 경도를 주소로 변환하여 출력
        var geocoder = new google.maps.Geocoder;
        geocoder.geocode({'location': pos}, function (results, status) {
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

// 이미지 미리보기
document.addEventListener('DOMContentLoaded', function() {
    const icon = document.getElementById('icon');
    const profileImage = document.getElementById('image-icon');
    const fileInput = document.getElementById('file-input');
    const profileField = document.getElementById('profile');

    // 이미지를 클릭했을 때 파일 선택창이 열리도록 이벤트 설정
    if (profileImage) {
        profileImage.addEventListener('click', function() {
            fileInput.click();
        });
    }

    if (icon) {
        icon.addEventListener('click', function() {
            fileInput.click();
        });
    }

    // 파일 선택이 변경되었을 때 호출되는 이벤트 핸들러
    // 파일 선택 후 미리보기 업데이트
    fileInput.addEventListener('change', function(event) {
        const file = event.target.files[0]; // 선택된 파일 가져오기
        if (file) {
            // FileReader 객체를 사용하여 이미지 파일을 읽음
            const reader = new FileReader();
            reader.onload = function(e) {
                // 읽은 데이터를 <img> 태그의 src 속성에 설정하여 미리보기 표시
                if (profileImage) {
                    profileImage.src = e.target.result;
                } else {
                    // 아이콘을 이미지로 변경
                    const newProfileImage = document.createElement('img');
                    newProfileImage.id = 'profile-image';
                    newProfileImage.className = 'profile';
                    newProfileImage.src = e.target.result;
                    newProfileImage.alt = '프로필 이미지';
                    icon.replaceWith(newProfileImage);

                    // 새로 생성된 이미지에 클릭 이벤트 리스너 추가
                    newProfileImage.addEventListener('click', function() {
                        fileInput.click();
                    });
                }
            };
            reader.readAsDataURL(file); // 이미지 파일을 Data URL 형태로 변환하여 읽음
        }
    });
});

// 주소로 위치 찾기
function searchAddress() {
    const inputAddress = document.getElementById('address-input').value;

    var geocoder = new google.maps.Geocoder();
    geocoder.geocode({'address': inputAddress}, function(results, status) {
        if (status === 'OK') {
            const pos = results[0].geometry.location;
            map.setCenter(pos);

            if (marker) {
                marker.setMap(null);
            }
            if (previousMarker) {
                previousMarker.setMap(null);
            }

            marker = new google.maps.Marker({
                position: pos,
                map: map,
                title: inputAddress
            });

            previousMarker = marker;

            // Hidden input에 값 설정
            document.getElementById('latitude').value = pos.lat();
            document.getElementById('longitude').value = pos.lng();
            document.getElementById('address').value = results[0].formatted_address;
        } else {
            console.error('Geocode was not successful for the following reason: ' + status);
        }
    });
}

// 비밀번호 확인
function checkPasswordMatch() {
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("checkPassword").value;
    var passwordMatchMessage = document.getElementById("checkpwd");

    if (password !== confirmPassword) {
        passwordMatchMessage.innerHTML = "비밀번호가 일치하지 않습니다.";
    } else {
        passwordMatchMessage.innerHTML = "";
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
    const form = document.getElementById('update-form');
    const formData = new FormData(form);

    const fileInput = document.getElementById('file-input');
    if (fileInput.files.length > 0) {
        formData.append('profile', fileInput.files[0]);
    } else {
        const profile = document.getElementById('profile').value;
        formData.append('profile', profile);
    }

    fetch(form.action, {
        method: 'PATCH',
        body: formData,
    }).then(response => {
        if (!response.ok) {
            // 기존 에러 메시지 제거
            document.querySelectorAll('.error-message').forEach(element => {
                element.textContent = '';
            });

            return response.json().then(errors => {
                // 서버로부터 받은 에러 메시지 표시
                Object.keys(errors).forEach(field => {
                    const errorMessageElement = document.getElementById(field + '-error');
                    if (errorMessageElement) {
                        errorMessageElement.textContent = errors[field];
                    }
                });
                throw new Error('정보수정에 실패하였습니다.');
            });
        }
        return response.json();
    }).then(data => {
        alert(data.message); // 서버 응답 메시지 ("정보수정에 성공하였습니다.")
        window.location.href = '/user/myPage'; // 마이페이지 메인으로 리디렉션
    }).catch(error => {
        alert(error.message);
    });
}