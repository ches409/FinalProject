<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CHECK-회원 가입</title>
    <link rel="stylesheet" href="resources/css/header.css" />
    <link rel="stylesheet" href="resources/css/signupform.css" />
    <script src="resources/js/signupCheck.js"></script>
</head>
<body>
    <div id="container">
<!--     
        <header>
            <div id="header1">
                <a href="#">
                    <h1>CHECK</h1>
                </a>
            </div>
            <div id="header3">
                <p>회원 정보 입력 <span>(필수 입력은 *로 표시됩니다.)</span></p>
                <hr>
            </div>
        </header>
 -->
		<jsp:include page="common/find_header.jsp">
 			<jsp:param name="headerType" value="noContent" />
 			<jsp:param name="pageTitle" value="회원 정보 입력 <span>(필수 입력은 *로 표시됩니다.)</span>"/>
 		</jsp:include>
        <main>
            <form action="signupProcess" method="POST" id="form">
                <table>
                    <tr>
                        <th>
                            <label for="m_acctid">아이디 <span>*</span></label>
                        </th>
                        <td>
                            <input type="text" name="m_acctid" id="m_acctid" required />
                            <p>영문 또는 숫자 조합 5~15자로 입력하세요.</p>
                            <p id="idmsg"></p>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="m_acctpwd">비밀번호 <span>*</span></label>
                        </th>
                        <td>
                            <input type="password" name="m_acctpwd" id="m_acctpwd" required />
                            <p>영문 대/소문자와 숫자, 특수문자 중 3가지의 조합 8~24자로 입력하세요.</p>
                            <p id="passerr"></p>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="confirmpwd">비밀번호 확인 <span>*</span></label>
                        </th>
                        <td>
                            <input type="password" name="confirmpwd" id="confirmpwd" required />
                            <p id="cfmmsg">위의 비밀번호를 다시 입력하세요.</p>
                            <p id="cfmerr"></p>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="m_name">성명 <span>*</span></label>
                        </th>
                        <td>
                            <input type="text" name="m_name" id="m_name" required />
                            <p id="namemsg">이름을 입력하세요.</p>
                            <p id="nameerr"></p>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="m_email">이메일 <span>*</span></label>
                        </th>
                        <td>
                            <input type="email" name="m_email" id="m_email" required />
                            <p>example@example.com 형식으로 입력하세요.</p>
                            <p id="emailmsg"></p>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="m_tel">전화번호</label>
                        </th>
                        <td>
                            <input type="tel" name="m_tel" id="m_tel" />
                            <p>010-1234-5678 형식으로 입력하세요.</p>
                            <p id="telerr"></p>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="m_dept">소속 / 조직</label>
                        </th>
                        <td>
                            <input type="text" name="m_dept" id="m_dept" />
                            <p>현재 소속을 입력하세요.</p>
                        </td>
                    </tr>
                </table>
                <hr />
                <button type="submit" id="signupBtn">가입</button>
            </form>
        </main>
    </div>
</body>
</html>