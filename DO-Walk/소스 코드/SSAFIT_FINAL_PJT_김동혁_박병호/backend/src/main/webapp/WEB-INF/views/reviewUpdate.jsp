<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>리뷰 수정</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        form { width: 60%; margin: 0 auto; }
        label { display: block; margin-top: 15px; font-weight: bold; }
        input[type="text"], textarea {
            width: 100%; padding: 10px; margin-top: 5px; border: 1px solid #ccc; border-radius: 5px;
        }
        .btns { margin-top: 20px; text-align: center; }
        .btn {
            padding: 10px 20px; background-color: #28a745; color: white; border: none; cursor: pointer;
        }
        .btn:hover { background-color: #218838; }
    </style>
</head>
<body>

<h2 style="text-align:center;">리뷰 수정</h2>

<form action="/review/update" method="post">
    <input type="hidden" name="reviewNo" value="${review.reviewNo}">
    <input type="hidden" name="videoNo" value="${review.videoNo}">
    <input type="hidden" name="writerId" value="${review.writerId}">
    <input type="hidden" name="writer" value="${review.writer}">

    <label for="title">제목</label>
    <input type="text" id="title" name="title" value="${review.title}" required>

    <label for="content">내용</label>
    <textarea id="content" name="content" rows="10" required>${review.content}</textarea>

    <div class="btns">
        <button type="submit" class="btn">수정하기</button>
    </div>
</form>

</body>
</html>
