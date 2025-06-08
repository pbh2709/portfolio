<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원 정보 수정</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 50px; }
        form { width: 400px; margin: 0 auto; }
        h2 { text-align: center; }
        label { display: block; margin-top: 15px; font-weight: bold; }
        input[type="text"], input[type="password"], input[type="email"], input[type="number"] {
            width: 100%; padding: 8px; margin-top: 5px;
        }
        .btns { margin-top: 20px; text-align: center; }
        .btn {
            padding: 10px 20px; background-color: #28a745; color: white;
            border: none; cursor: pointer;
        }
        .btn:hover { background-color: #218838; }
    </style>
</head>
<body>

<h2>회원 정보 수정</h2>

<form action="/user/update" method="post">
    <input type="hidden" name="userId" value="${user.userId}">

    <label>비밀번호</label>
    <input type="password" name="password" required>

    <label>이름</label>
    <input type="text" name="userName" value="${user.userName}" required>

    <label>이메일</label>
    <input type="email" name="email" value="${user.email}">

    <label>나이</label>
    <input type="number" name="age" value="${user.age}">

    <div class="btns">
        <input type="submit" value="수정 완료" class="btn">
    </div>
</form>

</body>
</html>
