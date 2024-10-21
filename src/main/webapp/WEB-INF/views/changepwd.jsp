<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CHECK-비밀번호 재설정</title>
    <link rel="stylesheet" href="resources/css/header.css" />
    <link rel="stylesheet" href="resources/css/changepwd.css" />
</head>
<body>
    <div id="container">
 		<jsp:include page="common/find_header.jsp">
 			<jsp:param name="headerType" value="content" />
 			<jsp:param name="pageTitle" value="비밀번호 재설정"/>
        	<jsp:param name="pageContent" value="비밀번호를 변경할 수 있습니다."/>
 		</jsp:include>
        <main>
            <form id="findProcess" action="<c:url value='/changepwdProcess' />" method="POST">
            	<c:choose>
            		<c:when test="${not empty member_id}">
				        <c:set var="member_id" value="${member_id}" />
				    </c:when>
            		<c:when test="${not empty param.member_id}">
				        <c:set var="member_id" value="${param.member_id}" />
				    </c:when>
            	</c:choose>
            	<input type="hidden" name="member_id" value="${member_id}" required />
                <table>
                    <tr>
                        <th>
                            <label for="newpwd">새 비밀번호</label>
                        </th>
                        <td>
                            <input type="password" name="newpwd" id="newpwd" required />
                            <p>새 비밀번호를 영문(대/소문자)과 숫자, 특수문자 중 3가지의 조합 8~24자로 입력하세요.</p>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="confirmpwd">새 비밀번호 확인</label>
                        </th>
                        <td>
                            <input type="password" name="confirmpwd" id="confirmpwd" required />
                            <p>새 비밀번호를 다시 입력하세요.</p>
                        </td>
                    </tr>
                </table>
	            <hr />
	            <div class="btn">
	                <button id="cancelBtn">취소</button>
	                <button type="submit" id="submitBtn">변경</button>
	            </div>
            </form>
        </main>
    </div>
</body>
</html>