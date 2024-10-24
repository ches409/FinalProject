<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="/resources/css/course_mu.css">
    <link rel="stylesheet" href="/resources/css/currentAttendance.css">
    <link rel="stylesheet" as="style" crossorigin
        href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/static/pretendard.min.css" />
    <script src="/resources/js/currentAttendance.js"></script>
</head>

<body>
    <div id="container">
        <jsp:include page="common/sidebar_course.jsp" />
        <main> 
         <div id="contentBox">
            <div id="attBox">
            <a href="/currentAttendance" id="allAtt" class="selectedAtt">출석 현황</a> <a href="setAttendance" id="setAtt" class="unselectedAtt"><div id="setAttText">온라인 출석부 설정</div></a>
            </div>
            <hr />

            <div id="searchArea">
                <form id="formBox" method="POST" action="/currentAttSearch">
                    <div id="searchFont">검색</div> 
                    <select name="searchType" id="searchType">
                        <option value="name">이름</option>
                        <option value="organization">소속</option>
                    </select>
                
                    <div class="search-box">
                    <input type="text" id="searchText" name="searchText" placeholder="검색어를 입력하세요." required />
                    <button id="submitBtn" type="submit" class="search-button">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50" width="20px" height="20px">
                            <path
                                d="M 21 3 C 11.601563 3 4 10.601563 4 20 C 4 29.398438 11.601563 37 21 37 C 24.355469 37 27.460938 36.015625 30.09375 34.34375 L 42.375 46.625 L 46.625 42.375 L 34.5 30.28125 C 36.679688 27.421875 38 23.878906 38 20 C 38 10.601563 30.398438 3 21 3 Z M 21 7 C 28.199219 7 34 12.800781 34 20 C 34 27.199219 28.199219 33 21 33 C 13.800781 33 8 27.199219 8 20 C 8 12.800781 13.800781 7 21 7 Z" />
                        </svg>
                    </button>
                    </div>
                </form>
            </div>
            
            <div>
                <div id="infoHeader">
                    <div class="infoArea1">번호</div>
                    <div class="infoArea2">이름</div>
                    <div class="infoArea3">소속</div>
                    <div class="infoArea4">출석</div>
                    <div class="infoArea4">지각</div>
                    <div class="infoArea4">결석</div>
                    <div class="infoArea4">점수</div>
                </div>

                <!-- for문으로 처리  -->
                <!-- status-bar 컴포넌트 화 -->
                <div id="infoList">
					<c:forEach items="${studentAttList}" var="studentAtt" varStatus="status" begin="${currAttPage*10}" end="${currAttPage*10 + 9}">
						<a href="/attendanceDetail?student_id=${studentAtt.student_id}">
						<div class="infoBox">
							<div class="infoArea1">${status.count+ currAttPage*10}</div>
							<div class="infoArea2" >${studentAtt.m_name}</div>
							<div class="infoArea3">${studentAtt.m_dept}</div>  
							<div class="infoArea4">${studentAtt.c}</div>
							<div class="infoArea4">${studentAtt.l}</div>
							<div class="infoArea4">${studentAtt.ab}</div>
							<div class="infoArea4">${courseScore.c_prsscore*studentAtt.c + courseScore.c_trdscore*studentAtt.l + courseScore.c_absscore*studentAtt.ab}</div>
                        </div>
						</a>
                    </c:forEach>                
                </div>
            </div>

			<div id="page">
				
				<c:choose>
					<c:when test="${currAttPage==0}">
						<button id="minusPage" disabled>이전</button>
					</c:when>
					<c:otherwise>
						<a href="/currentAttendance?currAttPage=${currAttPage-1}"><button id="minusPage" style="cursor:pointer">이전</button></a>	
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${studentAttList.size()==0}">
						<span id="targetPage" class="pageNumber">1</span>
					</c:when>
					<c:otherwise>
						<c:forEach  begin="0" end="${Math.floor((studentAttList.size()-1)/10)}" varStatus="status">
							<a href="/currentAttendance?currAttPage=${status.count-1}"><span id="targetPage" class="pageNumber">${status.count}</span></a>
						</c:forEach>
					</c:otherwise>
				</c:choose>
				
				
				<c:choose>
					<c:when test="${studentAttList.size()==0 || Math.floor((studentAttList.size()-1)/10) == currAttPage}">
						<button id="plusPage" disabled>다음</button>
					</c:when>
					<c:otherwise>
						<a href="/currentAttendance?currAttPage=${currAttPage+1}"><button id="plusPage" style="cursor:pointer">다음</button></a>
					</c:otherwise>
				</c:choose>

            </div>
         </div>
        </main>
        
    </div>
</body>

</html>