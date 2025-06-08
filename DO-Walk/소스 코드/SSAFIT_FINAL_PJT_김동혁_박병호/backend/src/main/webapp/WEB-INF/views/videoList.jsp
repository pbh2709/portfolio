<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비디오 목록</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .btn { padding: 8px 16px; background-color: #007BFF; color: white; border: none; cursor: pointer; margin-left: 10px; text-decoration: none; }
        .btn:hover { background-color: #0056b3; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: center; }
        th { background-color: #f2f2f2; }
        tr:hover { background-color: #f9f9f9; cursor: pointer; }
    </style>
</head>
<body>

<header>
    <div>
        <c:if test="${not empty loginUser}">
            <strong>${loginUser.userName}</strong> 님 안녕하세요!
            <a href="/user/mypage" class="btn">마이페이지</a>
            <a href="/user/logout" class="btn">로그아웃</a>
        </c:if>
    </div>
    <div>
        <a href="/video/form" class="btn">비디오 등록</a>
    </div>
</header>

<h2>비디오 목록</h2>

<table>
    <thead>
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>채널명</th>
            <th>조회수</th>
            <th>게시일</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="video" items="${videos}">
            <tr onclick="location.href='/video/detail/${video.videoNo}'">
                <td>${video.videoNo}</td>
                <td>${video.videoTitle}</td>
                <td>${video.channelName}</td>
                <td>${video.viewCount}</td>
                <td><fmt:formatDate value="${video.uploadDate}" pattern="yyyy-MM-dd"/></td>
            </tr>
        </c:forEach>
        <c:if test="${empty videos}">
            <tr>
                <td colspan="5">등록된 비디오가 없습니다.</td>
            </tr>
        </c:if>
    </tbody>
</table>

</body>
</html>
