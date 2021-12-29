<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
</head>
<link rel="stylesheet" href="css/form.css">
<script src="js/write.js"></script>
<body>
	<form action="BoardWriteProcCon.do" method="post">
		<table>
		<caption>글쓰기</caption>
			<tbody>
				<tr>
					<td class="label">작성자</td>
					<td class="content"><input type="text" name="writer"/></td>
				</tr>
				<tr>
					<td class="label">제목</td>
					<td class="content"><input type="text" name="subject"/></td>
				</tr>	
				<tr>
					<td class="label">이메일</td>
					<td class="content"><input type="text" name="email"/></td>
				</tr>
				<tr>
					<td class="label">비밀번호</td>
					<td class="content"><input type="password" name="password"/></td>
				</tr>
				<tr>	
					<td class="label">글내용</td>
					<td class="content"><textarea rows="10" cols="60" name="content"></textarea></td>
				</tr>
			</tbody>
		</table>
		<div class="btn">
			<input type="submit" value="글쓰기"/>&nbsp;&nbsp;
			<!-- enter 누르면 submit 막기 -->
			<input type="hidden"/>
			<input type="reset" value="취소"/>&nbsp;&nbsp;
			<!-- <button> 디폴트 submit 새로고침 새 글이 생기는 오류 -> type="button" 고침 -->
			<button type="button" onclick="location.href='BoardListContoller.do'">전체 게시글 보기</button>
		</div>
	</form>
</body>
</html>