 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin="anonymous"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/title.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/content.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/login.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/board.css">
<title>질문 게시판</title>
</head>
<body>
	<%@ include file="include/header.jsp" %>
	<center>
	<table width="75%" border="0" cellspacing="0" cellpadding="20">
		<tr>
			<td class="titlebox1">
				<span class="title01">STUDY CAFE</span>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center" class="content_text01"><font size="6">
				질문게시판
			</font></td>
		</tr>
		<tr>
			<td colspan="2" align="center" class="content_text01">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<center>
				<div class = "c">
				<table width="80%" border="0" cellspacing="0" cellpadding="10">
					<tr class="board_contentbox">
						<td class="content">
							<br><br>
							<center>
							<table width="80%" border="0" cellspacing="0" cellpadding="10">
								<tr>
									<th class="board_title"><center>글 번호</center></th>
									<th class="board_title" width="50%"><center>제목</center></th>
									<th class="board_title"><center>아이디</center></th>
									<th class="board_title"><center>등록일</center></th>
								</tr>
								<c:forEach items="${qdtos }" var="qdto">
								<tr>
									<td class="board_content01">${qdto.bnum }</td>
									<td class="board_content02">
									<a href="questionView?bnum=${qdto.bnum }">
									 
									<c:choose>
										<c:when test="${fn:length(qdto.btitle) > 23 }">
											<c:out value="${fn:substring(qdto.btitle,0,22) }"></c:out>...
										</c:when>
										<c:otherwise>
											<c:out value="${qdto.btitle }"></c:out>
										</c:otherwise>
									</c:choose>
									<c:if test="${qdto.rcount != 0 }">              
              						&nbsp;&nbsp;[${qdto.rcount }]
              						</c:if>
									</a>
									</td>
									<td class="board_content01">${qdto.buserid }</td>
									<td class="board_content01">
										<c:out value="${fn:substring(qdto.bdate,0,10) }"></c:out>										
									</td>
								</tr>						
								</c:forEach>
								<tr>
									<td colspan="5" align="center">
										<c:if test="${pageMaker.prev }">
											<a href="list?pageNum=${pageMaker.startPage-5 }">Prev</a>&nbsp;&nbsp;&nbsp;
										</c:if>										
										<c:forEach begin="${pageMaker.startPage }" end="${pageMaker.endPage }" var="num">
											<c:choose>
											<c:when test="${currPage == num}">
											<u>${num}</u>&nbsp;&nbsp;&nbsp;
											</c:when>
											<c:otherwise>
											<a href="list?pageNum=${num}">${num}</a>&nbsp;&nbsp;&nbsp;
											</c:otherwise>
											</c:choose>																					
										</c:forEach>
										<c:if test="${pageMaker.next }">
											<a href="list?pageNum=${pageMaker.startPage+5 }">Next</a>
										</c:if>	
									</td>
								</tr>	
								<tr>
									<td colspan="5" align="right">
										<input type="button" value="질문하기" class="button_type01" onclick="script:window.location='question'">
									</td>
								</tr>
								
							</table>
							</center>
							<br>						
						</td>						
					</tr>
					
				</table>
				</div>
				</center>			
			</td>
		</tr>
	</table>
	</center>
	<%@ include file="include/footer.jsp" %>
</body>
</html>