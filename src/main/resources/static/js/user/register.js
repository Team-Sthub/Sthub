// 지도 띄우기
let map;
var address;
var file;
var marker;
var previousMarker;
var changeLat;
var changeLng;
var password;
var confirmPassword;

function initMap() {
    // 지도 생성
    map = new google.maps.Map(document.getElementById('map'), {
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

            marker = new google.maps.Marker({
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

// 페이지가 로드될 때 initMap 함수를 호출
window.onload = function() {
    initMap();
};

// 페이지가 로드될 때 또는 리다이렉트 후 initMap 함수를 다시 호출
window.onpageshow = function(event) {
    if (event.persisted) {
        initMap();
    }
};

// 중복 확인 버튼을 누를 때의 동작 설정
document.getElementById("checkId").addEventListener("click", function() {
    const responseNickname = document.getElementById('nickname').value;
    console.log(responseNickname);
    console.log("인코딩 " + encodeURIComponent(responseNickname));
    fetch(`/user/register/check?nickname=${encodeURIComponent(responseNickname)}`, {
        method: 'GET',
    }).then(response => {
        if (response.ok) {
            return response.json();
        } else if (response.status === 409) {
            throw new Error('이미 사용 중인 아이디입니다.');
        } else {
            throw new Error('아이디 중복 체크 중 오류가 발생했습니다.');
        }
    }).then(data => {
        const responseElement = document.getElementById('response');
        responseElement.style.color = 'red';
        responseElement.style.fontSize = '12px';
        responseElement.textContent = data.data;
    }).catch(error => {
        const errorMessage = error.message;
        const errorMessageElement = document.getElementById('response');
        errorMessageElement.style.color = 'red';
        errorMessageElement.style.fontSize = '12px';
        errorMessageElement.textContent = errorMessage;
        console.error('Error:', error);
    });
});

// 주소로 위치 찾기
function searchAddress() {
    const inputAddress = document.getElementById('address-input').value;

    var geocoder = new google.maps.Geocoder;
    geocoder.geocode({'address': inputAddress}, function(results, status) {
        if (status === 'OK') {
            const pos = results[0].geometry.location;
            map.setCenter(pos);

            if (marker) {       // 현재 위치에 대한 마커 제거
                marker.setMap(null);
            }
            if (previousMarker) {       // 기존 검색 마커 제거
                previousMarker.setMap(null);
            }

            marker = new google.maps.Marker({
                position: pos,
                map: map,
                title: inputAddress
            });

            previousMarker = marker;
            // Hidden input에 값 설정
            const formattedAddress = results[0].formatted_address;

            // '대한민국'을 제외한 나머지 주소 부분 추출
            const addressWithoutCountry = formattedAddress.replace(/^대한민국\s*/, '');

            //displayAddress(addressWithoutCountry);
            document.getElementById('address-input').value = addressWithoutCountry;
            document.getElementById('latitude').value = pos.lat();
            changeLat = pos.lat();
            console.log("지도 검색 위도는 " + pos.lat());
            document.getElementById('longitude').value = pos.lng();
            changeLng = pos.lng();
            console.log("지도 검색 위도는 " + pos.lng());

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

// 비밀번호 확인
function checkPasswordMatch() {
    password = document.getElementById("password").value;
    confirmPassword = document.getElementById("checkPassword").value;
    var passwordMatchMessage = document.getElementById("checkpwd");

    if (password !== confirmPassword) {
        passwordMatchMessage.innerHTML = "비밀번호가 일치하지 않습니다.";
    } else {
        passwordMatchMessage.innerHTML = "";
    }
}

// 위치 정보를 가져와 폼 필드에 설정한 후 폼을 제출
function getLocationAndSubmit() {
    const addressInput = document.getElementById('address-input');
    const addressField = document.getElementById('address');

    // 주소 입력 따로 안하고 현재 위치로 회원가입 할 때
    if (addressInput.value !== "") {
        addressField.value = addressInput.value;
        document.getElementById('latitude').value = changeLat;
        document.getElementById('longitude').value = changeLng;
        submitForm();

    } else {
        addressField.value = address;
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
        if (password !== confirmPassword) {
            throw new Error('회원가입에 실패하였습니다.');
        }

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
                throw new Error('회원가입에 실패하였습니다.');
            });
        }
        return response.json();
    }).then(data => {
        alert(data.message); // 서버 응답 메시지 ("회원가입에 성공하였습니다.")
        window.location.href = '/user/login'; // 로그인 페이지로 리디렉션
    }).catch(error => {
        alert(error.message);
    });
}