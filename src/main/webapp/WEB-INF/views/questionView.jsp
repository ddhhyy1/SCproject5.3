<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin="anonymous"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/title.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/content.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/login.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/board.js"></script>

<title>내 질문</title>
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
				<table width="80%" border="0" cellspacing="0" cellpadding="10">
					<tr class="contentbox">
						<td class="content">
							<center>						
							<table border="0" cellspacing="0" cellpadding="10">
								<form action="questionModify" method="post" name="board_frm">
									<input type="hidden" value="${qdto.bnum }" name="bnum">
									<tr>
										<td><span class="content_text01">아 이 디 : </span></td>
										<td><input class="input_type01" type="text" name="buserid" value="${qdto.buserid }" readonly="readonly"></td>
									</tr>
									<tr>
										<td><span class="content_text01">제목 : </span></td>
										<td><input class="input_type01" type="text" name="btitle" value="${qdto.btitle }" readonly="readonly"></td>
									</tr>
									<tr>
										<td><span class="content_text01">질문내용 : </span></td>
										<td><textarea class="textarea_type01" rows="5" cols="30" name="bcontent" readonly="readonly">${qdto.bcontent}</textarea></td>
									</tr>									
									
									<tr>
										<td><span class="content_text01">등 록 일 : </span></td>
										<td><input class="input_type01" type="text" name="bdate" value="${qdto.bdate }" readonly="readonly"></td>
									</tr>
									<tr>
										<td colspan="2">
										<%
											String boardId = (String) request.getAttribute("buserid");
										
											if(boardId.equals("GUEST")) {
										%>
										<input class="button_type01" type="submit" value="수정">&nbsp;&nbsp;										
										<input class="button_type01" type="button" value="삭제" onclick="script:window.location='questionDelete?bnum=${qdto.bnum}'">
										<input class="button_type01" type="button" value="글목록" onclick="script:window.location='list'">
										<%
											} else if((sessionId != null ) && (sessionId.equals(boardId))) {
										%>
											<input class="button_type01" type="submit" value="수정">&nbsp;&nbsp;
											<input class="button_type01" type="button" value="삭제" onclick="script:window.location='questionDelete?bnum=${qdto.bnum}'">
											<input class="button_type01" type="button" value="글목록" onclick="script:window.location='list'">
										<%
											} else {
										%>
											<input class="button_type01" type="button" value="글목록" onclick="script:window.location='list'">
										<%
											}
										%>
										</td>
									</tr>
								</form>
								</table>
								</center>
								
	<center>	
		
       
        
		<table border="1" cellpadding="0" cellspacing="0" width="750">
        	<c:forEach items="${replylist }" var="replyDtoa">
        	<tr>
        		
        		<td width=550>
        			${replyDtoa.rcontent }<br><br>
        			
        		</td>
        		<td>
        			<a href="replyDelete?rnum=${replyDtoa.rnum }&bnum=${qdto.bnum }">삭제</a>
        		</td>
        		
        	</tr>
        	</c:forEach>
        	</table>
        	
        
        <table>
        	<form action="replyOk" method="post">
        <input type="hidden" name="bnum" value="${qdto.bnum }">
									<tr>
										<td><span class="content_text01">댓글 : </span></td>
										<td><textarea class="textarea_type01" rows="10" cols="30" name="rcontent"></textarea></td>
									</tr>	
									<tr>
										<td colspan="2">
											
											<button type="submit" class="btn btn-primary btn-lg">댓글등록</button>
										</td>
									</tr>
						
									</table>
									</form>
			</center>
			
			
			</td>
								</tr>
								</table>
								</center>
								</td>
								</tr>
								</table>
								</center>
	<%@ include file="include/footer.jsp" %>
</body>
</html>