/**
 * 회원가입 유효성 검사
 */

function validateM_acctid() {
	const m_acctid = document.querySelector('#m_acctid');
	const idmsg = document.querySelector('#idmsg');
	let valid = true;
		
	idmsg.style.color = 'red';
	
	if(!m_acctid.value.trim()) {
		idmsg.innerHTML = "아이디를 입력하세요.";
		valid=false;
	}
	else if(m_acctid.value.length < 5 || m_acctid.value.length > 15) {
		idmsg.innerHTML = "아이디의 길이가 유효하지 않습니다.";
		valid=false;
	}
	else {
		idmsg.innerHTML = "";
		checkIdDuplication(m_acctid.value);
	}
	return valid;
}

function checkIdDuplication(id) {
	const xhr = new XMLHttpRequest();
	xhr.open('GET', `/checkM_acctidDuplicate?m_acctid=${encodeURIComponent(id)}`, true);
	
	xhr.onload = function() {
		if(xhr.status === 200) {
			const isAvailable = JSON.parse(xhr.responseText);
			const idmsg = document.querySelector('#idmsg');
			if(isAvailable) {
				idmsg.innerHTML = "사용 가능한 아이디입니다.";
				idmsg.style.color = 'blue';
			}
			else {
				idmsg.innerHTML = "이미 사용 중인 아이디입니다.";
				idmsg.style.color = 'red';
			}
		}
		else {
			alert('아이디 확인 중 오류가 발생했습니다.');
		}
	};
	
	xhr.onerror = function() {
		alert('서버와의 통신에 실패했습니다.');
	};
	
	xhr.send();
}

function validatePassword() {
	const m_acctpwd = document.querySelector('#m_acctpwd');
	const confirmpwd = document.querySelector('#confirmpwd');
	const passerr = document.querySelector('#passerr');
	const cfmmsg = document.querySelector('#cfmmsg');
	const cfmerr = document.querySelector('#cfmerr');
	
	const pwdUpper = /[A-Z]/.test(m_acctpwd.value);
	const pwdLower = /[a-z]/.test(m_acctpwd.value);
	const pwdNumber = /[0-9]/.test(m_acctpwd.value);
	const pwdSpecial = /[!@#$%^&*]/.test(m_acctpwd.value);
	
	let valid=true;
	let isAvail = [pwdUpper, pwdLower, pwdNumber, pwdSpecial].filter(Boolean).length;
	
	passerr.style.color = 'red';
	cfmerr.style.color = 'red';
	
	//비밀번호 입력
	if(!m_acctpwd.value.trim()) {
		passerr.innerHTML = "비밀번호를 입력하세요.";
		valid = false;
	}
	else if(m_acctpwd.value.length < 8 || m_acctpwd.value.length > 24 || isAvail < 3) {
		passerr.innerHTML = "비밀번호의 길이가 유효하지 않거나, 조합 요건을 충족하지 못했습니다.";
		valid=false;
	}
	else {
		passerr.innerHTML = "유효한 비밀번호입니다.";
		passerr.style.color = 'blue';
	}
	
	//비밀번호 확인
	if(!confirmpw.value.trim()) {
		cfmmsg.style.color= 'red';
		cfmerr.innerHTML = "";
		valid = false;
	}
	else if(m_acctpwd.value !== confirmpw.value) {
		cfmmsg.style.color= '#888888';
		cfmerr.innerHTML = "비밀번호가 일치하지 않습니다.";
		valid = false;
	}
	else {
		cfmerr.innerHTML = "";
	}
	
	return valid;
}

function validateM_email() {
	const m_email = document.querySelector('#m_email');
	const emailmsg = document.querySelector('#emailmsg');
	const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

	let valid = true;
		
	emailmsg.style.color = 'red';
	
	if(!m_email.value.trim()) {
		emailmsg.innerHTML = "이메일을 입력하세요.";
		valid=false;
	}
	else if(!emailPattern.test(m_email.value)) {
		emailmsg.innerHTML = "올바른 형식의 이메일을 입력하세요.";
		valid=false;
	}
	else {
		emailmsg.innerHTML = "";
		checkEmailDuplication(m_email.value);
	}
	return valid;
}

function checkEmailDuplication(email) {
	const xhr = new XMLHttpRequest();
	xhr.open('GET', `/checkM_emailDuplicate?m_email=${encodeURIComponent(email)}`, true);
	
	xhr.onload = function() {
		if(xhr.status === 200) {
			const isAvailable = JSON.parse(xhr.responseText);
			const emailmsg = document.querySelector('#emailmsg');
			if(isAvailable) {
				emailmsg.innerHTML = "사용 가능한 이메일입니다.";
				emailmsg.style.color = 'blue';
			}
			else {
				emailmsg.innerHTML = "이미 사용 중인 이메일입니다.";
				emailmsg.style.color = 'red';
			}
		}
		else {
			alert('이메일 확인 중 오류가 발생했습니다.');
		}
	};
	
	xhr.onerror = function() {
		alert('서버와의 통신에 실패했습니다.');
	};
	
	xhr.send();
}

function validateM_name() {
	const m_name = document.querySelector('#m_name');
	const namemsg = document.querySelector('#namemsg');
	const nameerr = document.querySelector('#nameerr');
	
	let valid=true;
	
	nameerr.style.color = 'red';
	
	if(!m_name.value.trim()) {
		namemsg.style.color = 'red';
		nameerr.innerHTML = "";
		valid=false;
	}
	else if(m_name.value.length > 10) {
		nameerr.innerHTML = "이름의 길이가 유효하지 않습니다.";
		valid=false;
	}
	else {
		namemsg.style.color = '#888888';
		nameerr.innerHTML = "";
	}
	return valid;
}

function validateM_tel() {
	const m_tel = document.querySelector('#m_tel');
	const telerr = document.querySelector('#telerr');
	const telPattern = /^\d{3}-\d{3,4}-\d{4}$/;
	let valid = true;
	telerr.style.color = 'red';
	
	if(!m_tel.value.trim()) {
		telerr.innerHTML = "";
	}
	else if(!telPattern.test(m_tel.value)) {
		telerr.innerHTML = "올바른 형식의 전화번호를 입력하세요.";
		valid=false;
	}
	else {
		telerr.innerHTML = "";
	}
	return valid;
}

function init() {
	const form = document.querySelector('form');
	
	document.querySelector('#m_acctid').addEventListener('input', validateM_acctid);
	document.querySelector('#m_acctpwd').addEventListener('input', validatePassword);
	document.querySelector('#confirmpwd').addEventListener('input', validatePassword);
	document.querySelector('#m_email').addEventListener('input', validateM_email);
	document.querySelector('#m_name').addEventListener('input', validateM_name);
	document.querySelector('#m_tel').addEventListener('input', validateM_tel);

    form.addEventListener('submit', function(event) {
        if (!validateM_acctid() || !validatePassword() || !validateM_email() || !validateM_name() || !validateM_tel) {
            event.preventDefault();
        }
		else {
			alert('회원가입이 완료되었습니다.');
		}
    });
}

window.addEventListener('load', init);