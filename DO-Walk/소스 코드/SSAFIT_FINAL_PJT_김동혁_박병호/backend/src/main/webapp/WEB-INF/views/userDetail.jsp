<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { width: 400px; margin: 0 auto; border-collapse: collapse; }
        th, td { padding: 12px; border-bottom: 1px solid #ccc; text-align: left; }
        h2 { text-align: center; }
        .btns { text-align: center; margin-top: 20px; }
        .btn {
            padding: 10px 20px; background-color: #007BFF; color: white;
            border: none; text-decoration: none;
            margin-right: 10px;
        }
        .btn:hover { background-color: #0056b3; }
        .btn-red { background-color: #dc3545; }
        .btn-red:hover { background-color: #c82333; }
    </style>
</head>
<body>

<h2>마이페이지</h2>

<table>
    <tr>
        <th>아이디</th>
        <td>${user.userId}</td>
    </tr>
    <tr>
        <th>이름</th>
        <td>${user.userName}</td>
    </tr>
    <tr>
        <th>이메일</th>
        <td>${user.email}</td>
    </tr>
    <tr>
        <th>나이</th>
        <td>${user.age}</td>
    </tr>
</table>

<div class="btns">
    <a href="/user/updateForm" class="btn">정보 수정</a>
    <a href="/user/delete/${user.userId}" class="btn btn-red" onclick="return confirm('정말 탈퇴하시겠습니까?');">회원 탈퇴</a>
    <a href="/video/list" class="btn">비디오 목록</a>
</div>

</body>
</html>
