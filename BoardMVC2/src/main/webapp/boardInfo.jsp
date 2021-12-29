<%@page import="model.BoardBean"%>
<%@page import="model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 보기</title>
</head>
<link rel="stylesheet" href="css/info.css">
<body>
	<table>
	<caption>게시글 보기</caption>
		<tbody>
			<tr>
				<td class="label">글번호</td>
				<td class="content">${bean.num}</td>
				<td class="label">조회수</td>
				<td class="content">${bean.readCount}</td>
			</tr>
			<tr>
				<td class="label">작성자</td>
				<td class="content">${bean.writer}</td>
				<td class="label">작성일</td>
				<td class="content">${bean.regDate}</td>
			</tr>
			<tr>
				<td class="label">이메일</td>
				<td class="content" colspan="3">${bean.email}</td>
			</tr>
			<tr>
				<td class="label">제목</td>
				<td class="content" colspan="3">${bean.subject}</td>				
			</tr>
			<tr>
				<td class="label">내용</td>
				<td class="content" colspan="3">${bean.content}</td>
			</tr>
		</tbody>
	</table>
	<!-- Controller를 들렸다가 JSP로 가야한다. 데이터를 읽어들이기 때문에 -->
	<div class="btn">
		<button type="button" onclick="location.href='BoardReWriteCon.do?num=${bean.num}&ref=${bean.ref}&reStep=${bean.reStep}&reLevel=${bean.reLevel}'">
		답글쓰기</button>
		<!-- num null 오류 num 안보내서 오류남 -->
		<button type="button" onclick="location.href='BoardUpdateCon.do?num=${bean.num}'">수정하기</button>
		<button type="button" onclick="location.href='BoardDeleteCon.do?num=${bean.num}'">삭제하기</button>
		<button type="button" onclick="location.href='BoardListContoller.do'">목록보기</button>
	</div>
</body>
</html>