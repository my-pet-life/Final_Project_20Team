document.getElementById('logout').addEventListener('click', function (event) {
    const token = localStorage.getItem('accessToken');
    // alert(token);

    // 토큰 데이터 삭제
    localStorage.removeItem("accessToken");
    alert("로그아웃 되었습니다.");
});