

const NONE_JOB = " = select = ";
const DATE_TIME_FORMAT = "DD MMM YYYY HH:mm";
const INVALID_DATE = "Invalid date";
const MSECS_PER_MIN = 60000;

	var timeoutID;
	 
	function startLogoutTimer() {
	    this.addEventListener("mousemove", resetTimer, false);
	    this.addEventListener("mousedown", resetTimer, false);
	    this.addEventListener("keypress", resetTimer, false);
	    this.addEventListener("DOMMouseScroll", resetTimer, false);
	    this.addEventListener("mousewheel", resetTimer, false);
	    this.addEventListener("touchmove", resetTimer, false);
	    this.addEventListener("MSPointerMove", resetTimer, false);
	 
	    startTimer();
	}
	
	
	//startLogoutTimer();
	
	
	function startTimer() {
	    // wait 2 seconds before calling goInactive
	    timeoutID = window.setTimeout(goInactive, 5*60000); // mins logout
	}
	 
	function resetTimer(e) {
	    window.clearTimeout(timeoutID);
	 
	    goActive();
	}
	 
	function goInactive() {
		// can logout???
	   location.href="login"
	}
	 
	function goActive() {
	    // do something	         
	    startTimer();
	}
	                                              // txt = display, butt = VERB/button text

//double quote str
function dqstr(val){
	return '"'+val+'"';
}

//single quote str
function sqstr(val){
	return "'"+val+"'";
}


function clearFormErrors(attrib){

	for (const field in attrib) {
		var widget = $('#'+field );		
		widget.removeClass("is-invalid");
		widget.addClass("is-valid")

	}
		
}


function validateForm(attrib){
    const regex=/^[0-9\.]+$/;
	var postObject={};
	 var errorcount = 0
	 //var i = 0, len = attrib.length; i < len; i++
	for (const field in attrib) {
		var widget = $('#'+field );
		var widgetval = $('#'+field ).val();
		widgetval = widgetval!=undefined ? widgetval.trim() : "";
		console.log(field + " => " + widgetval + " " + attrib[field]);
		var fieldError = 0;
		postObject[field] = widgetval;				
		switch(attrib[field]){
			case 'string':				
			//	if (widgetval==""){
			//		fieldError += 1
			//	} 
			     break;
			case 'number':				
    			if (!widgetval.match(regex)){		  	    
					fieldError += 1
				} 
		  	    break;
		  	case 'select':				
    			if (!widgetval.match(regex)){		  	    
					fieldError += 1
				}else if (Number.parseInt(widgetval) <= 0){
					fieldError += 1
				}
		  	    break;
			case 'date': 				
				if (moment( widgetval, [DATE_TIME_FORMAT]).format() == INVALID_DATE){
					fieldError += 1;
				}
				break;
			case 'checkbox':
				postObject[field] = widget.is(':checked')
				break;
			}			     
		if (fieldError>0){
			// mark in red	
			console.log('field '+field+' has problem ' + attrib[field])
			widget.addClass("is-invalid") //background-color:#fdb9bc;
			widget.removeClass("is-valid");
			errorcount += 1			
		}else{
			widget.removeClass("is-invalid");
			widget.addClass("is-valid")
		}
	}
	postObject.errorcount = errorcount;
	return postObject;
}
