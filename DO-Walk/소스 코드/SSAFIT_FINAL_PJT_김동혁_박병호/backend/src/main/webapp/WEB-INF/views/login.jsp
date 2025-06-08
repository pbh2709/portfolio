<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 50px; }
        form { width: 300px; margin: 0 auto; }
        h2 { text-align: center; }
        label { display: block; margin-top: 15px; font-weight: bold; }
        input[type="text"], input[type="password"] {
            width: 100%; padding: 8px; margin-top: 5px;
        }
        .btns { margin-top: 20px; text-align: center; }
        .btn {
            padding: 10px 20px; background-color: #007BFF; color: white;
            border: none; cursor: pointer; margin-right: 10px;
        }
        .btn:hover { background-color: #0056b3; }
        .msg { color: red; text-align: center; margin-top: 20px; }
    </style>
</head>
<body>

<h2>로그인</h2>

<form action="/user/login" method="post">
    <label>아이디</label>
    <input type="text" name="userId" required>

    <label>비밀번호</label>
    <input type="password" name="password" required>

    <div class="btns">
        <input type="submit" value="로그인" class="btn">
        <a href="/user/form" class="btn">회원가입</a>
    </div>

    <c:if test="${not empty sessionScope.msg}">
        <div class="msg">${sessionScope.msg}</div>
        <c:remove var="msg" scope="session"/>
    </c:if>
</form>

</body>
</html>