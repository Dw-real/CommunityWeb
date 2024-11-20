document.getElementById("create").addEventListener('click', function() {
    location.href = "/user/create";
});

document.getElementById("login").addEventListener('click', function() {
    location.href = "/login";
});

document.getElementById("findId").addEventListener('click', function() {
    location.href = "/user/findId";
});

document.getElementById("findPwd").addEventListener('click', function() {
    location.href = "/user/findPwd";
});

document.getElementById('userId').addEventListener('click', function() {
    document.getElementById('dialog-overlay').style.display = document.getElementById('dialog-overlay').style.display === "block" ? "none" : "block";
    document.getElementById('userIdDialog').style.display = document.getElementById('userIdDialog').style.display === "block" ? "none" : "block";
});

document.getElementById('dialog-overlay').addEventListener('click', function() {
    document.getElementById('dialog-overlay').style.display = 'none';
    document.getElementById('userIdDialog').style.display = 'none';
});

document.getElementById('viewPosts').addEventListener('click', function() {

});

document.getElementById("updatePwd").addEventListener('click', function() {
    location.href = "/user/updatePwd";
});

function toggleDisplay(loggedIn, userId) {
    if (loggedIn) { // 로그인 성공
        document.getElementById('create').style.display = 'none';
        document.getElementById('login').style.display = 'none';
        document.getElementById('findId').style.display = 'none';
        document.getElementById('findPwd').style.display = 'none';
        document.getElementById('userId').style.display = 'inline';
        document.getElementById('userId').textContent = userId + "님";
        document.getElementById('updatePwd').style.display = 'inline';
        document.getElementById('logout').style.display = 'inline';
        document.getElementById('postBtn').style.display = 'inline';
    } else {
        document.getElementById('create').style.display = 'inline';
        document.getElementById('login').style.display = 'inline';
        document.getElementById('findId').style.display = 'inline';
        document.getElementById('findPwd').style.display = 'inline';
        document.getElementById('userId').style.display = 'none';
        document.getElementById('updatePwd').style.display = 'none';
        document.getElementById('logout').style.display = 'none';
        document.getElementById('postBtn').style.display = 'none';
    }
}

function posting() {
    location.href = "/community/post";
}

$(document).ready(function() {
    $('#logout').click(function() {
        $.ajax({
            url: '/logout',
            type: 'GET',
            success: function(response) {
                // 로그아웃 후 화면 업데이트
                window.location.reload();  // 페이지를 새로 고침하여 로그인 상태를 업데이트
            },
            error: function() {
                alert('로그아웃 중 오류가 발생했습니다.');
            }
        });
    });

    const items = document.querySelectorAll('.board-item');

    items.forEach(item => {
        item.addEventListener('click', () => {
            event.preventDefault();

            items.forEach(i => i.classList.remove('active'));
            item.classList.add('active');

        });
    });
});