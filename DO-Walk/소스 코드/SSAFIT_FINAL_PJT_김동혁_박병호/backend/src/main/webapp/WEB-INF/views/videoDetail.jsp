<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비디오 상세 보기</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { width: 60%; margin: 0 auto; border-collapse: collapse; }
        th, td { padding: 12px; border-bottom: 1px solid #ccc; text-align: left; }
        h2 { text-align: center; }
        .btns { text-align: center; margin-top: 20px; }
        .btn { padding: 10px 20px; background-color: #007BFF; color: white; border: none; text-decoration: none; margin-right: 10px; }
        .btn:hover { background-color: #0056b3; }
    </style>
</head>
<body>

<h2>비디오 상세 정보</h2>

<table>
    <tr>
        <th>제목</th>
        <td>${video.videoTitle}</td>
    </tr>
    <tr>
        <th>채널명</th>
        <td>${video.channelName}</td>
    </tr>
    <tr>
        <th>조회수</th>
        <td>${video.viewCount}</td>
    </tr>
    <tr>
        <th>게시일</th>
        <td>${video.uploadDate}</td>
    </tr>
    <tr>
        <th>좋아요 여부</th>
        <td><c:choose>
                <c:when test="${video.videoLike == 1}">좋아요</c:when>
                <c:otherwise>싫어요</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>분야</th>
        <td>${video.field}</td>
    </tr>
    <tr>
        <th>운동 부위 번호</th>
        <td>${video.fitnessPartNo}</td>
    </tr>
</table>

<div class="btns">
    <a href="/video/list" class="btn">목록으로</a>

    <c:if test="${not empty loginUser}">
        <a href="/video/update/${video.videoNo}" class="btn">수정</a>
        <a href="/video/delete/${video.videoNo}" class="btn" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
    </c:if>
</div>

<!-- 해당 영상 리뷰 리스트 -->
<h2 style="margin-top: 50px;">이 영상에 대한 리뷰</h2>
<table>
    <thead>
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="review" items="${reviewList}">
            <tr onclick="location.href='/review/detail?id=${review.reviewNo}'" style="cursor:pointer;">
                <td>${review.reviewNo}</td>
                <td>${review.title}</td>
                <td>${review.writer}</td>
                <td>${review.regDate}</td>
            </tr>
        </c:forEach>

        <c:if test="${empty reviewList}">
            <tr>
                <td colspan="4">이 영상에 대한 리뷰가 아직 없습니다.</td>
            </tr>
        </c:if>
    </tbody>
</table>

<c:if test="${not empty loginUser}">
    <div class="btns">
        <a href="/review/writeform?videoNo=${video.videoNo}" class="btn">리뷰 작성</a>
    </div>
</c:if>

</body>
</html>