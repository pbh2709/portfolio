<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비디오 수정</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        form { max-width: 600px; margin: 0 auto; }
        label { display: block; margin-top: 15px; font-weight: bold; }
        input[type="text"], input[type="number"], select {
            width: 100%; padding: 8px; margin-top: 5px;
        }
        input[type="submit"] {
            margin-top: 20px; padding: 10px 20px;
            background-color: #007BFF; color: white;
            border: none; cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<h2>비디오 수정</h2>

<form action="/video/update" method="post">
    <input type="hidden" name="videoNo" value="${video.videoNo}">

    <label>제목</label>
    <input type="text" name="videoTitle" value="${video.videoTitle}" required>

    <label>채널명</label>
    <input type="text" name="channelName" value="${video.channelName}" required>

    <label>분야</label>
    <input type="text" name="field" value="${video.field}">

    <label>운동 부위 번호</label>
    <input type="number" name="fitnessPartNo" value="${video.fitnessPartNo}" required>

    <input type="submit" value="수정하기">
</form>

</body>
</html>
