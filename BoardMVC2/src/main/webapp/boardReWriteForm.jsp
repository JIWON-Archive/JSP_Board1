<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>답변 글쓰기</title>
</head>
<link rel="stylesheet" href="css/form.css">
<body>
	<form action="BoardReWriteProcCon.jsp" method="post">
		<table>
			<caption>답변 글쓰기</caption>
			<tbody>
				<tr>
					<td class="label">작성자</td>
					<td class="content"><input type="text" name="writer"/></td>
				</tr>
				<tr>
					<td class="label">제목</td>
					<td class="content"><input type="text" name="subject" value="[답변]"/></td>
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
		<!-- form에서 사용자로부터 입력받지 않고 데이터를 연결 -->
		<div class="btn">
			<input type="hidden" name="ref" value="${ref}"/>
			<input type="hidden" name="reStep" value="${reStep}"/>
			<input type="hidden" name="reLevel" value="${reLevel}"/>
			<input type="submit" value="답글 쓰기"/>&nbsp;&nbsp;
			<input type="reset" value="취소"/>&nbsp;&nbsp;
			<button type="button" onclick="location.href='boardList.jsp'">전체 게시글 보기</button>
		</div>
	</form>
</body>
</html>