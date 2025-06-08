<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>리뷰 상세</title>
</head>
<body>

<h2>리뷰 상세</h2>

<table border="1" cellpadding="10">
    <tr>
        <th>제목</th>
        <td>${review.title}</td>
    </tr>
    <tr>
        <th>작성자 아이디</th>
        <td>${review.writerId}</td>
    </tr>
    <tr>
        <th>내용</th>
        <td>${review.content}</td>
    </tr>
    <tr>
        <th>작성일</th>
        <td>${review.regDate}</td>
    </tr>
</table>

<br>
<a href="/video/detail/${review.videoNo}">리뷰 목록으로</a>


    <a href="/review/updateform?id=${review.reviewNo}">수정</a>
    <a href="/review/delete?id=${review.reviewNo}" onclick="return confirm('삭제할까요?');">삭제</a>


</body>
</html>
