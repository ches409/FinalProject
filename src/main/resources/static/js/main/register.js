function responseHandler(error, response) {
	if (error === null) {
		if (response.res) {
			alert('요청이 정상적으로 처리되었습니다.')
		} else {
			alert(response.msg);
		}
		window.location.reload();
	} else {
		console.error(error);
	}
}

function sendRequest(url, method, callback) {
	const xhr = new XMLHttpRequest();
	xhr.open(method, url, true);

	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if (xhr.status === 200) {
				const response = JSON.parse(xhr.responseText);
				callback(null, response);
			} else {
				callback(new Error(`AJAX 요청 실패: ${xhr.status}`));
			}
		}
	};

	xhr.send();
}

function clickHandler(courseId, courseName) {
	if (confirm(`[${courseName}] 강의를 수강 신청하시겠습니까?\n수강 신청이 승인된 이후부터 수업에 참여 가능합니다.`)) {
		const url = '/api/course/register?courseId=' + courseId;
		sendRequest(url, 'GET', responseHandler);
	}
}

function init() {
	const courseItems = document.querySelectorAll('.tab.course#register .item');
	for (const item of courseItems) {
		const courseId = item.dataset.id;
		const courseName = item.querySelector('.name').innerHTML;
		const enterBtn = item.querySelector('.enter');

		enterBtn.addEventListener('click', () => {
			clickHandler(courseId, courseName);
		});
	}
}

window.addEventListener('load', init);