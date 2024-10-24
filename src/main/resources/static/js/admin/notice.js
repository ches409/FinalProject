function responseHandler(error, response) {
	if (error === null) {
		if (response.res) {
			alert(response.msg)
			window.location.href = "/admin/notice";
		} else {
			alert(response.msg);
			return false;
		}
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

function clickButtonHandler(event) {
	const id = event.target.id;
	switch (id) {
		case 'write':
			window.location.href = "/admin/notice/write";
			break;
		case 'delete':
			if (confirm('게시글을 삭제하시겠습니까?')) {
				const postId = event.target.dataset.id;
				const url = "/admin/notice/delete?postId=" + postId;
				sendRequest(url, 'GET', responseHandler);
			}
			break;
		case 'list':
			window.location.href = `/admin/notice?page=${event.target.dataset.page}`;
			break;
	}
}

function clickPostHandler(item) {
	const postId = item.dataset.id;
	const page = document.querySelector('.page.selected').innerHTML;
	window.location.href = `/admin/notice/details?postId=${postId}&page=${page}`;
}

function cancelHandler(event) {
	event.preventDefault();
	const form = document.querySelector('form');
	const formData = new FormData(form);
	let allBlank = true;

	for (let [key, value] of formData.entries()) {
		if (value.trim()) {
			allBlank = false;
			break;
		}
	}
	if (!allBlank) {
		if (confirm('작성했던 내용이 지워집니다. 글 작성을 취소하시겠습니까?')) {
			window.history.back();
		} else {
			return false;
		}
	} else {
		widnow.history.back();
	}
}
function createPreview(link) {
	const previewDiv = document.createElement('div');
	previewDiv.classList.add('preview');
	previewDiv.id = 'preview';
	previewDiv.innerHTML = '';

	link.addEventListener('click', function(event) {
		event.preventDefault();
		const url = link.href;
		const ext = url.split('.').pop().toLowerCase();
		if (['jpg', 'jpeg', 'png', 'gif'].includes(ext)) {
			const img = document.createElement('img');
			img.src = url;
			img.alt = 'Image Preview';
			img.style.maxWidth = '100%';
			previewDiv.appendChild(img);
		} else if (ext === 'pdf') {
			const embed = document.createElement('embed');
			embed.src = url;
			embed.type = 'application/pdf';
			embed.style.width = '100%';
			embed.style.height = '600px';
			previewDiv.appendChild(embed);
		} else if (ext === 'mp4') {
			const video = document.createElement('video');
			video.src = url;
			video.controls = true;
			video.style.width = '100%';
			previewDiv.appendChild(video);
		} else if (ext === 'txt' || ext === 'html') {
			fetch(url)
				.then(response => response.text())
				.then(text => {
					const pre = document.createElement('pre');
					pre.textContent = text;
					previewDiv.appendChild(pre);
				});
		} else {
			previewDiv.textContent = '해당 유형의 파일은 미리보기를 제공하지 않습니다.';
		}
	});

	return previewDiv;
}

function init() {
	try {
		const items = document.querySelectorAll('.tab.notice .item');
		for (const item of items) {
			item.addEventListener('click', () => {
				clickPostHandler(item);
			});
		}
	} catch (e) {
	}
	try {
		const btns = document.querySelectorAll('.btn');
		for (const btn of btns) {
			btn.addEventListener('click', clickButtonHandler);
		}
	} catch (e) {
	}
	try {
		const cancelBtn = document.querySelector('#cancel');
		cancelBtn.addEventListener('click', cancelHandler);
	} catch (e) {
	}
	try {
		const attms = document.querySelectorAll('.attms li');
		for (const attm of attms) {
			const link = attm.querySelector('.attm');
			attm.appendChild(createPreview(link));
		}
	} catch (e) {
		console.error(e);

	}
}

window.addEventListener('load', init);