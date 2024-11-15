    function logIn() {
        const userId = document.getElementById("userId").value;
        const userPwd = document.getElementById("userPwd").value;
        const inputNotice = document.getElementById("inputNotice");

        if (!userId) {
            inputNotice.textContent = "아이디를 입력해주세요.";
            return;
        } else if (!userPwd) {
            inputNotice.textContent= "비밀번호를 입력해주세요.";
            return;
        }

        // CSRF 토큰 가져오기
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

        // AJAX 요청을 통해 로그인 시도
        $.ajax({
            type: 'POST',
            url: '/loginTry',
            headers: {
                'X-CSRF-TOKEN': csrfToken,
                'Content-Type' : 'application/json'
            },
            data: JSON.stringify({
                id: userId,
                pwd: userPwd
            }),
            success: function(response) {
                window.location.href = "/";
            },
            error: function(error) {
                inputNotice.textContent = "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.";
            }
        });
    }