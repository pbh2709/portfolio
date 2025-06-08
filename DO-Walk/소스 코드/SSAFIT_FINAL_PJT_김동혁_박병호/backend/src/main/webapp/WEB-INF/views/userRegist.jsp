<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
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

<h2>회원가입</h2>

<form action="/user/regist" method="post">
    <label>아이디</label>
    <input type="text" name="userId" required>

    <label>비밀번호</label>
    <input type="password" name="password" required>

    <label>이름</label>
    <input type="text" name="userName" required>

    <label>이메일</label>
    <input type="email" name="email">

    <label>나이</label>
    <input type="number" name="age" min="0">

    <div class="btns">
        <input type="submit" value="가입하기" class="btn">
    </div>
</form>

</body>
</html>
