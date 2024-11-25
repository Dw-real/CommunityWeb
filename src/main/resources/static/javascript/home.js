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
            loadBoardList(item.id, 0);
        });
    });
});

function loadBoardList(type, page) {
    $.ajax({
        url: `/community/board/${type}`,
        type: 'GET',
        data: {page: page, size: 10},
        success: function(data) {
            renderBoardList(data.content);
            renderPagination(data);
        },
        error: function(error) {

        }
    });
}

function loadBoardList(type, page) {
        $.ajax({
            url: `/community/board/${type}`,
            type: "GET",
            data: { page: page, size: 10 },
            success: function (data) {
                renderBoardList(data.content);
                renderPagination(data);
            },
            error: function (error) {
                console.error("Error loading board list:", error);
            }
        });
    }

function renderBoardList(boardList) {
    let tableBody = $("table tbody");
    tableBody.empty();

    boardList.forEach(board => {
       let date = new Date(board.boardCreatedTime);
       let formattedDate = date.getFullYear() + '-' +
                           String(date.getMonth() + 1).padStart(2, '0') + '-' +
                           String(date.getDate()).padStart(2, '0') + ' ' +
                           String(date.getHours()).padStart(2, '0') + ':' +
                           String(date.getMinutes()).padStart(2, '0');

        let boardRow = `
            <tr>
                <td>${board.boardType === 'FREE' ? '자유게시판' : 'Q&A게시판'}</td>
                <td style="text-align: left;"><a href="/community/${board.id}?page=${board.page}" style="color: inherit; text-decoration: none;">
                                                  ${board.boardTitle}</a></td>
                <td>${board.boardWriter}</td>
                <td>${formattedDate}</td>
                <td>${board.boardHits}</td>
            </tr>
        `;
        tableBody.append(boardRow);
    });
}

function renderPagination(data) {
    let pagination = $(".pagination");
    pagination.empty(); // 기존 페이지네이션 제거

    let startPage = data.number - 2 > 0 ? data.number - 2 : 1;
    let endPage = startPage + 4 < data.totalPages ? startPage + 4 : data.totalPages;

    let navHtml = `
        <a href="#" onclick="loadBoardList('${data.boardType}', 0)"><<</a>
        <a href="#" onclick="loadBoardList('${data.boardType}', ${data.number - 1})"><</a>
    `;

    for (let i = startPage; i <= endPage; i++) {
        if (i === data.number + 1) {
            navHtml += `<span class="current-page">${i}</span>`;
        } else {
            navHtml += `<a href="#" onclick="loadBoardList('${data.boardType}', ${i - 1})">${i}</a>`;
        }
    }

    navHtml += `
        <a href="#" onclick="loadBoardList('${data.boardType}', ${data.number + 1})">></a>
        <a href="#" onclick="loadBoardList('${data.boardType}', ${data.totalPages - 1})">>></a>
    `;

    pagination.html(navHtml);
}