let today;

function dialogHandler() {
    let dialog = document.querySelector('#modal');
    let dateBoxArr = document.querySelectorAll('.weekBox>td');
    let reqType = document.querySelector('#reqType');
    let reqDate = document.querySelector('#reqDate');
    let exit = document.querySelector('.exit');
    let yearMonth = document.querySelector('#yearMonth');

    for(let dateBox of dateBoxArr){
        dateBox.addEventListener('mousedown', () => {
            console.log(dateBox.innerHTML);
        }
        );
        dateBox.addEventListener('mouseup', (event) => {
            console.log(dateBox.innerHTML);
            if(dateBox.getAttribute('class') === 'monthDate-l' || dateBox.getAttribute('class') === 'monthDate-ab'){
                dialog.open=true ;
                reqType.innerHTML='출결 정정 요청';
                reqDate.innerHTML= yearMonth.innerText + '.' + dateBox.firstChild.innerText;
            } 
        }
        );
    }
    
    

    exit.addEventListener('click', () => {
        dialog.open = false;
    });


}

function getDateColor(a_status){
	if(a_status==1){
		return 'monthDate-a';
	}else if(a_status==2){
		return 'monthDate-ab';
	}else if(a_status==3){
		return 'monthDate-l';
	}else{
		return 'monthDate-nc';
	}
}

function getDateText(req_type, r_status){
	if(req_type==1){
		if(r_status == 0){
			return '정정요청중';
		}else if(r_status == 1){
			return '정정요청승낙';
		}else{
			return '정정요청거절';
		}
	}else if(req_type==2){
		if(r_status == 0){
			return '공가요청중';
		}else if(r_status == 1){
			return '공가요청승낙';
		}else{
			return '공가요청거절';
		}
	}else{
		return '';
	}
	
}

function getCalender(number){
    const yearMonth = document.querySelector('#yearMonth');
    const calendarBox = document.querySelector('#calendarBox');
	
	let dateTime=new Date();
	let c_sdate = new Date(document.querySelector('#c_sdate').value);
	let c_edate = new Date(document.querySelector('#c_edate').value);
	
	if(dateTime.getFullYear() < c_sdate.getFullYear()) {
		dateTime=c_sdate;
	}else if(dateTime.getFullYear() > c_edate.getFullYear()){
		dateTime=c_edate;
	}else if(dateTime.getFullYear() == c_edate.getFullYear() && dateTime.getMonth() > c_edate.getMonth()) {
		dateTime=c_edate;
	}else if(dateTime.getFullYear() == c_sdate.getFullYear() && dateTime.getMonth() < c_sdate.getMonth()) {
		dateTime=c_sdate;
	}
	today=dateTime;
	
    today.setMonth(today.getMonth() + number);
//자바스크립트에서 promise 객체란 비동기 방식을 처리하기위한 객체
	fetch('getStudentCalendar?c_year='+today.getFullYear()+'&c_month='+ (today.getMonth()+1)) // url만 지정하면 get 방식, 2번째 매개변수는 option객체
	.then(response => response.json()) // 비동기 처리의 결과를 then이 받는다.
	.then(obj => {
		let firstDate = new Date(new Date(obj[0].dt).getFullYear(), new Date(obj[0].dt).getMonth(), 1);
	    let lastDate= new Date(new Date(obj[0].dt).getFullYear(), new Date(obj[0].dt).getMonth()+1, 0);
	    let prevLastDate = new Date(new Date(obj[0].dt).getFullYear(), new Date(obj[0].dt).getMonth(), 0);
	    let firstDay = firstDate.getDay();
	    let lastDay = lastDate.getDay();
	    let prevLastDay = prevLastDate.getDay();
	    let weekCount = (lastDate.getDate()-(7-firstDay + lastDay+1))/7+2
	    
	    yearMonth.innerText = today.getFullYear() + '.' + ('0'+(today.getMonth()+1)).slice(-2);
	    
	 
	    let firstWeekBox = '<tr class="weekBox">';
	    if(firstDay!=0){
	    for(let i = prevLastDay; i >= 0; i--){
	        if(i==prevLastDay){
	            firstWeekBox += '<td class="preMonthDate-ww">'+ (prevLastDate.getDate()-i) +'</td>';
	        }else{
	            firstWeekBox += '<td class="preMonthDate">'+ (prevLastDate.getDate()-i) +'</td>';
	        }
	    }

	    for(let i = 1; i <= 6-prevLastDay; i++){
	        if(i==6-prevLastDay){
	            firstWeekBox += '<td class="monthDate-w">'+i+'</td>';
	        }else{
	            firstWeekBox += '<td class="' + getDateColor(obj[i-1].a_status) + '">'+i+'</td>';
	        }
	        
	    }
	    }else{
	        for(let i = 1; i<=7; i++){
	            if(i==1){
	                firstWeekBox += '<td class="monthDate-ww">'+i+'</td>';
	            }else if(i==7){
	                firstWeekBox += '<td class="monthDate-w">'+i+'</td>';
	            }else{
	                firstWeekBox += '<td class="' + getDateColor(obj[i-1].a_status) + '">'+i+'</td>';
	            }
	        }
	    }
	    firstWeekBox +='</tr>';


	    let lastWeekBox ='<tr class="weekBox">';

	    for(let i = lastDay; i >= 0; i--){
	        if(i==lastDay){
	            lastWeekBox += '<td class="monthDate-ww">'+ (lastDate.getDate()-i) +'</td>';
	        }else if(lastDay==6 && i==0){
	            lastWeekBox += '<td class="monthDate-w">'+ (lastDate.getDate()) +'</td>';
	        }else{
	            lastWeekBox += '<td class="' + (obj.length>=lastDate.getDate()-i ? getDateColor(obj[lastDate.getDate()-i-1].a_status) : 'monthDate-nc') + '">'+ (lastDate.getDate()-i) +'</td>';
	        }
	    }

	    if(lastDay != 6){
	        for(let i = 1; i <= 6-lastDay; i++){
	            lastWeekBox += '<td class="preMonthDate">'+i+'</td>';
	        }
	    }
	    lastWeekBox +='</tr>';
	                    
		
	    let weekBoxs='';
	    for(j = 8-firstDay; j < lastDate.getDate()-lastDay; j++){ 
	        if(j%7==(8-firstDay)%7){
	            weekBoxs += '<tr class="weekBox"><td class="monthDate-ww">'+ j +'</td>';
	        }else if(j%7==(14-firstDay)%7){
	            weekBoxs += '<td class="monthDate-w">'+ j +'</td></tr>';
	        }else{
	            weekBoxs += '<td class="' + getDateColor(obj[j-1].a_status) + '">'+ j +'</td>';
	        }
	        
	    }  
		
		
								
		calendarBox.innerHTML=firstWeekBox + weekBoxs + lastWeekBox;
			
	}) // 변환된 자바스크립트 객체 obj == response.json()
	.catch(error => console.log(error));
		
    
}

function mobileHandler(){
    let menuList = document.querySelector('#menuList');
    let menuIcon = document.querySelector('#mobile-menu-icon');
    let menuName = document.querySelector('.menuName');
    let mobileMenu = document.querySelector('#mobile-menu');
    let className = menuName.innerHTML;
    menuIcon.addEventListener('click', () => {
        menuList.style.getPropertyValue('visibility') ==='hidden'? menuList.style.setProperty('visibility', 'visible') : menuList.style.setProperty('visibility', 'hidden');       
        menuList.style.getPropertyValue('visibility') ==='hidden'? menuName.innerHTML=className : menuName.innerHTML='CHECK';
    });
}


function init() {
	let num = 0;
	let c_sdate = new Date(document.querySelector('#c_sdate').value);
	let c_edate = new Date(document.querySelector('#c_edate').value);
	
    document.querySelector('#leftMonth').addEventListener('click', (event) =>{today.getFullYear()==c_sdate.getFullYear() && today.getMonth() == c_sdate.getMonth()? event.preventDefault() : getCalender(--num)});
    document.querySelector('#rightMonth').addEventListener('click', (event) =>{today.getFullYear()==c_edate.getFullYear() && today.getMonth() == c_edate.getMonth() ? event.preventDefault() : getCalender(++num)});
    document.querySelector('#todayBox').addEventListener('click', () =>{num=0; getCalender(num)});
	getCalender(num);
    mobileHandler();
    dialogHandler();
}

window.addEventListener('load', init);