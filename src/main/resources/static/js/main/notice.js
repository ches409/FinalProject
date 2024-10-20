/**
 * 게시글 클릭 시 공지사항 게시글 상세 조회를 제공하는 스크립트 코드
 */

let currentId = -1;

function getFileNameFromUrl(url) {
	// URL의 마지막 부분을 추출하여 파일 이름을 반환합니다.
	return url.substring(url.lastIndexOf('/') + 1);
}

function createPost(item) {
	const post = document.createElement('div');
	const contents = document.createElement('pre');
	const attms = document.createElement('ul');

	post.classList.add('post');
	contents.classList.add('content');
	attms.classList.add('attms');

	if (item.postContents === null) {
		item.postContents = '';
	}

	if (item.attachments != null) {
		let attachments = item.attachments.split(',');
		for (let attm of attachments) {
			let li = document.createElement('li');
			let a = document.createElement('a');

			a.innerHTML = '[첨부파일] ' + getFileNameFromUrl(attm);
			a.href = attm;
			a.download = getFileNameFromUrl(attm);

			li.appendChild(a);
			attms.appendChild(li);
		}
	}

	contents.innerHTML = item.postContents.trim() !== '' ? item.postContents : item.postTitle;

	post.appendChild(contents);
	post.appendChild(document.createElement('hr'));
	post.appendChild(attms);

	return post;
}

function createRow(item) {
	let tr = document.createElement('tr');
	let td = document.createElement('td');

	/* tr 태그에 대한 속성 추가 */
	tr.classList.add('item');
	tr.classList.add('details');

	/* tr 태그의 자식인 td 태그에 대한 속성 추가 */
	td.appendChild(createPost(item));
	tr.append(td);

	return tr;
}

function insertAfter(parent, newRow, currentRow) {
	parent.insertBefore(newRow, currentRow.nextSibling);
}

let parentNode, currentNode;

function responseHandler(error, response) {
	if (error === null) {
		insertAfter(parentNode, createRow(response), currentNode);
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

function clickHandler(item) {
	const itemId = item.dataset.id;

	currentNode = item;
	parentNode = item.parentNode;

	if (currentId === itemId) {
		return false;
	} else {
		currentId = itemId;
		const details = document.querySelector('tr.details');
		if (details !== null) {
			parentNode.removeChild(details);
		}
		const url = '/api/notice/getItem?noticeId=' + itemId;
		sendRequest(url, 'GET', responseHandler);
	}
}

function noticeHandler() {
	let items = document.querySelectorAll('tr.item');

	for (let item of items) {
		item.addEventListener('click', () => {
			clickHandler(item);
		});
	}
}

window.addEventListener('load', noticeHandler);