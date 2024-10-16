<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<aside class="sidebar main cffffff">
	<div class="flex cen v h logo p50">
		<div>
			<h1 class="f32">
				<a href="/">CHECK</a>
			</h1>
			<p class="inst">FOR INSTRUCTORS</p>
		</div>
		<div class="mobile" id="hamburger">
			<svg class="icon" xmlns="http://www.w3.org/2000/svg" width="30px"
				height="30px" viewBox="0 0 24 24" fill="none">
                <path d="M4 6H20M4 12H20M4 18H20" stroke="#000000"
					stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
		</div>
	</div>
	<ul class="menulist" id="main">
		<a href="/?t=2"><c:set var="main" value="main"></c:set>
			<li
			class="menu <c:if test="${menu eq main}">selected</c:if>
				cffffff">코스
		</li> </a>
		<a href="/checkin?t=2"><c:set var="checkin" value="checkin"></c:set>
			<li
			class="menu <c:if test="${menu eq checkin}">selected</c:if>
				cffffff">출석
				체크</li> </a>
		<a href="/alert"><c:set var="alert" value="alert"></c:set>
			<li
			class="menu <c:if test="${menu eq alert}">selected</c:if>
				cffffff">알림
		</li> </a>
		<a href="/notice?t=2"><c:set var="notice" value="notice"></c:set>
			<li
			class="menu <c:if test="${menu eq notice}">selected</c:if>
				cffffff">공지사항
		</li> </a>
		<a href="/mypage"><c:set var="mypage" value="mypage"></c:set>
			<li
			class="menu <c:if test="${menu eq mypage}">selected</c:if>
				cffffff">마이
				페이지</li> </a>
		<a href="/logout">
			<li class="menu cffffff">로그아웃</li>
		</a>
	</ul>
</aside>