function findUserId() {
    const name = document.getElementById("userName").value;
    const email = document.getElementById("userEmail").value;

    const showUserInfoDiv = document.getElementById("show-userInfo");
    const foundUserId = document.getElementById("foundUserId");

    if (!name || !email) {
        alert("이름, 이메일을 모두 입력해주세요");
        return;
    }

    var header = $("meta[name='_csrf_header']").attr('content');
    var token = $("meta[name='_csrf']").attr('content');

    $.ajax({
        type: "POST",
        url:  '/user/findId',
        contentType:'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        data: JSON.stringify({
            name: name,
            email: email
        }),
        success: function(response) {
            if (response) {
                showUserInfoDiv.style.display = "block";
                foundUserId.textContent = response;
            } else {
                alert("회원 정보를 가져오는 중 예상치 못한 문제가 발생했습니다.");
            }
        },
        error: function(xhr, status, error) {
            try {
                // 서버에서 반환된 오류 메시지를 추출하여 alert로 표시
                const errorResponse = JSON.parse(xhr.responseText);
                alert(errorResponse.errors.join("\n"));
            } catch (e) {
                alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
            }
        }
    });
}

function findUserPwd() {
    const id = document.getElementById("userId").value;
    const email = document.getElementById("userEmail").value;

    const updatePwd = document.getElementById("updatePwd");

    if (!id || !email) {
        alert("아이디와 이메일을 모두 입력하세요");
        return;
    }

    var header = $("meta[name='_csrf_header']").attr('content');
    var token = $("meta[name='_csrf']").attr('content');

    $.ajax({
        type: "POST",
        url:  '/user/findPwd',
        contentType:'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        data: JSON.stringify({
            id: id,
            email: email
        }),
        success: function(response) {
            if (response) {
                updatePwd.style.display = "block";
                document.getElementById("userId").readOnly = true;
            } else {
                alert("회원 정보를 가져오는 중 예상치 못한 문제가 발생했습니다.");
            }
        },
        error: function(xhr, status, error) {
            try {
                // 서버에서 반환된 오류 메시지를 추출하여 alert로 표시
                const errorResponse = JSON.parse(xhr.responseText);
                alert(errorResponse.errors.join("\n"));
            } catch (e) {
                alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
            }
        }
    });
}

function updatePwd() {
    const id = document.getElementById("userId").value;
    const newPwd = document.getElementById("newPwd").value;
    const checkPwd = document.getElementById("checkNewPwd").value;

    if (!newPwd || !updatePwd) {
        notice.textContent = "모든 칸을 입력해주세요."
        return;
    } else if (newPwd !== checkPwd) {
        notice.textContent = "새 비밀번호를 정확히 입력해주세요.";
        return;
    }

    var header = $("meta[name='_csrf_header']").attr('content');
    var token = $("meta[name='_csrf']").attr('content');

    $.ajax({
        type: "PATCH",
        url:  '/user/findPwd',
        contentType:'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        data: JSON.stringify({
            id: id,
            newPwd: newPwd
        }),
        success: function(response) {
            alert("비밀번호가 변경되었습니다.");
            location.href ="/";
        },
        error: function(xhr, status, error) {
            try {
                // 서버에서 반환된 오류 메시지를 추출하여 alert로 표시
                const errorResponse = JSON.parse(xhr.responseText);
                alert(errorResponse.errors.join("\n"));
            } catch (e) {
                alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
            }
        }
    });
}

function checkComplete() {
    const showUserInfoDiv = document.getElementById("show-userInfo");
    showUserInfoDiv.style.display = "none";
}