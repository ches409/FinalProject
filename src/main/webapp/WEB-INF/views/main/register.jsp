<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>CHECK-수강신청</title>
<link rel="stylesheet" href="/resources/css/common.css">
<link rel="stylesheet" href="/resources/css/main/main.css">
<link rel="stylesheet" as="style" crossorigin
	href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/static/pretendard.min.css" />
<script src="/resources/js/main/mobile.js"></script>
<script src="/resources/js/main/register.js"></script>
</head>
<body>
	<div class="container flex">
		<!-- 메인 사이드바 -->
		<jsp:include page="../common/side_main.jsp"></jsp:include>
		<!-- 본문 -->
		<main class="contents bgf2f2f2">
			<div class="grid g20">
				<h3 class="f24">수강 가능 강의 목록</h3>
				<!-- 서치바 영역 -->
				<jsp:include page="./register_searchbar.jsp"></jsp:include>
				<table class="tab course" id="register">
					<c:forEach items="${list }" var="item">
						<tr class="item" data-id="${item.courseId }">
							<td class="deco"></td>
							<td class="category">${item.categoryName }</td>
							<td class="name">${item.courseName }</td>
							<td class="info">
								<p>
									<strong>기간</strong> ${item.startDate }-${item.endDate }
								</p>
								<p>
									<strong>정원</strong> ${item.count }/${item.limits }(명)
								</p>
							</td>
							<td class="enter"><button>신청하기</button></td>
						</tr>
					</c:forEach>
				</table>
				<ul class="pagination">
					<c:if test="${page ne 1 }">
						<a href="/register?page=${page-1 }">
							<li class="page">이전</li>
						</a>
					</c:if>
					<c:forEach var="i" begin="1" end="${size }">
						<a href="/register?page=${i}">
							<li class="page <c:if test="${i eq page }">selected</c:if>">${i}</li>
						</a>
					</c:forEach>
					<c:if test="${page ne size}">
						<a href="/register?page=${page+1 }">
							<li class="page">다음</li>
						</a>
					</c:if>
				</ul>
			</div>
		</main>
	</div>
</body>

</html>