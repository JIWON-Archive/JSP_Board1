<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
</head>
<link rel="stylesheet" href="css/list.css">
<body>
<c:if test="${msg == 1}">
	<script type="text/javascript">
		alert("수정 시 비밀번호가 틀렸습니다.");
		history.go(-1);
	</script>
</c:if>
<c:if test="${msg == 2}">
	<script type="text/javascript">
		alert("삭제 시 비밀번호가 틑렸습니다.");
		history.go(-1);
	</script>
</c:if>
	<table>
		<caption>전체 게시글 보기</caption>
		<tr>
			<td>번호</td>
			<td>제목</td>
			<td>작성자</td>
			<td>작성일</td>
			<td>조회수</td>
		</tr>
		<c:set var="number" value="${number}" />
		<!-- 	// ArrayList에 저장되어 있는 빈 클래스를 하나씩 추출 -->
		<!-- 확장 for문과 같다. list를 받아서 하나씩 bean타입으로 뽑아줌 -->
		<c:forEach var="bean" items="${list}">
			<tr>
				<td>${number}</td>
				<td><c:if test="${bean.reStep > 1}">
						<c:forEach var="j" begin="1" end="${(bean.reStep -1) * 2}">
								&nbsp;				
						</c:forEach>
					</c:if> <a id="subject" href="BoardInfoControl.do?num=${bean.num}">${bean.subject}</a>
				</td>
				<td>${bean.writer}</td>
				<td>${bean.regDate}</td>
				<td>${bean.readCount}</td>
			</tr>
			<c:set var="number" value="${number-1}" />
		</c:forEach>
	</table>
	<div class="write-btn">
		<button onclick="location.href='boardWriteForm.jsp'">글쓰기</button>
	</div>
	<p>
	<!-- 페이지 카운터링 소스 -->
	<c:if test="${count>0}">
		<!-- 카운터링 숫자를 얼마까지 보여줄건지 결정 -->
		<c:set var="pageCount" value="${count/pageSize + (count % pageSize == 0 ? 0 : 1)}"/>
		<c:set var="startPage" value="${1}"/>
		<c:if test="${currentPage % 10 != 0}">
			<!-- 결과를 정수형으로 리턴 받아야하기에 fmt -->
			<fmt:parseNumber var="result" value="${(currentPage/10)}" integerOnly="true"/>
			<c:set var="startPage" value="${result * 10 + 1}"/>
		</c:if>
		<c:if test="${currentPage % 10 == 0}">
			<c:set var="startPage" value="${(result-1) * 10 + 1}"/>
		</c:if>
		<!-- 화면에 보여질 페이지 처리 숫자를 표현 -->
		<c:set var="pageBlock" value="${10}"/>
		<c:set var="endPage" value="${startPage + pageBlock - 1 }"/>
		<c:if test="${endPage > pageCount}">
			<c:set var="endPage" value="${pageCount}"/>
		</c:if>
		<c:if test="${startPage > 10}">
			<a class="count" href="BoardListContoller.do?pageNum=${startPage-10}">[이전]</a>
		</c:if>
		<!-- 페이징 처리 -->
		<c:forEach var="i" begin="${startPage}" end="${endPage}">
			<a class="count" href="BoardListContoller.do?pageNum=${i}">[${i}]</a>
		</c:forEach>
		<c:if test="${endPage > pageCount}">
			<a class="count" href="BoardListContoller.do?pageNum${startPage + 10}">[다음]</a>
		</c:if>
	</c:if>
	</p>
</body>
</html>