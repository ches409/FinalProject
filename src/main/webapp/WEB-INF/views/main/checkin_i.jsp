<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>CHECK-출석체크</title>
<link rel="stylesheet" href="/resources/css/common.css">
<link rel="stylesheet" href="/resources/css/main/checkin.css">
<link rel="stylesheet" href="/resources/css/mobile.css">
<link rel="stylesheet" as="style" crossorigin
	href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/static/pretendard.min.css" />
<script src="/resources/js/main/mobile.js"></script>
<script src="/resources/js/main/checkin.js"></script>
<body>
	<div class="container flex">
		<!-- 메인 사이드바 -->
		<jsp:include page="../common/side_main_i.jsp"></jsp:include>
		<!-- 본문 -->
		<main class="contents bgf2f2f2">
			<div class="grid g10 mbe30">
				<p class="f24 bold">출석 체크</p>
				<p>
					학생 전체 출결 정보를 보고 싶다면? <a class="bold" href="#">📝 출결 정보 상세보기</a>
				</p>

			</div>
			<div class="grid c2 g10">
				<div class="grid g20 fig full">
					<p class="f20 bold">분류 > ${info.categoryName }</p>
					<p class="f30 bold">${info.courseName }</p>
					<div class="info">
						<p>
							<span>강의 기간:</span><span class="float-right">${info.startDate }
								~ ${info.endDate }</span>
						</p>
						<p>
							<span>강사:</span><span class="float-right"><c:forEach
									items="${info.instList }" var="inst" varStatus="status">${inst }</c:forEach></span>
						</p>
					</div>
					<p>
						종료일까지 남은 기간: <span class="bold">${info.dDay }</span>
					</p>
					<a class="f20 bold h end self"
						href="/goCourseHome?courseId=${info.courseId }">📖 강의 홈으로 이동하기</a>
				</div>
				<table class="tab ta cen inst">
					<tr>
						<td class="qrBtn" id="new"><span class="f24">QR 발급</span></td>
					</tr>
				</table>
				<div class="fig qr">
					<div class="ta cen">
						<c:if test="${not empty info.qrCode }">
							<div class="grid c2 cen v h">
								<div class="">
									<p>QR코드 만료까지 남은 시간</p>
									<p class="bold" id="timeLimit" data-end="${info.qrEffdate }"></p>
								</div>
								<div>
									<img class="img s200"
										src="/api/checkin/getQRImage?id=${info.courseId }" alt="">
								</div>
							</div>
						</c:if>
						<c:if test="${empty info.qrCode }">
							<div class="p30">현재 유효한 출석 QR코드가 존재하지 않습니다.</div>
						</c:if>
					</div>
				</div>
				<div class="grid g20 fig full">
					<p class="f20 bold">출결 현황</p>
					<div class="grid c4 ta cen fig">
						<div class="border-right">
							<p>출석</p>
							<p class="bold">${stats.presentCnt }</p>
						</div>
						<div class="border-right">
							<p>지각</p>
							<p class="bold">${stats.tardyCnt }</p>
						</div>
						<div class="border-right">
							<p>조퇴</p>
							<p class="bold">${stats.leaveCnt }</p>
						</div>
						<div>
							<p>결석</p>
							<p class="bold">${stats.absentCnt }</p>
						</div>
					</div>
					<div class="grid g10">
						<p>
							<span class="bold">금일 출석률</span> <span> <fmt:formatNumber
									value="${stats.myCnt/stats.totalCnt }" type="percent"
									pattern="0.0%"></fmt:formatNumber>
							</span> <span> (${stats.myCnt }0/${stats.totalCnt }일)</span>
						</p>
						<!-- 차트 라이브러리 연결 후 수정-->
						<div class="chart grid cen v">
							<div class="grid cen h" id="avg">
								<p>
									<b>평균</b> <span id="avgPer"><fmt:formatNumber
											value="${stats.avgCnt/stats.totalCnt }" type="percent"
											pattern="0.0%"></fmt:formatNumber></span>
								</p>
								<div class="line"></div>
							</div>
							<div class="bullet box bgd9d9d9">
								<div class="bullet bar"></div>
							</div>
							<div class="grid cen h" id="curr">
								<div class="line"></div>
								<p>
									<b>금일 출석률</b> <span id="myPer"><fmt:formatNumber
											value="${stats.myCnt/stats.totalCnt }" type="percent"
											pattern="0.0%"></fmt:formatNumber></span>
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</main>
	</div>
</body>

</html>