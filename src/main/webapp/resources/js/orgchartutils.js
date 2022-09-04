

const NONE_JOB = " = select = ";
const DATE_TIME_FORMAT = "DD MMM YYYY HH:mm";
const INVALID_DATE = "Invalid date";
const MSECS_PER_MIN = 60000;

	var timeoutID;


var prefs = { // set default settings
	bins : {
		MAX_COLS : 9,
		MAX_ORD_BIN : 7, // max orders per bin
	},
	table : {
		scrollCollapse : false,
		paging : true,
		refresh : 1,
	},
	vsite : [],
	vsiteall : [],
};

function initFilt() {

	//console.log("site prefs:" + JSON.stringify(prefs));

	prefs.vsiteall.forEach(function(item) {
		setButton($('#site' + item), true);
	});
	prefs.vsite.forEach(function(item) {
		setButton($('#site' + item), true);
	});	
}

function getFilterStr() {
	var vlist = [];
	var str = "site=";	
	if (prefs) {
		
		prefs.vsiteall.forEach(function(item) {
			if ($('#site' + item).attr('checked')) {
				vlist.push(item);
				str += item + ",";
			}
		});

		prefs.vsite = vlist;
		// str += $("#jobsmy").attr('checked') ? "&m" : "";
		// prefs.vfilt.jobsmy = $("#jobsmy").attr('checked') ? true : false;
	}
	
	return str;
}



	 
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

//double quote str
function dqstr(val){
	return '"'+val+'"';
}

//single quote str
function sqstr(val){
	return "'"+val+"'";
}


function stdError(errorThrown){
	$('#msgdialog').modal('show');
	$('#msgdetails').html('<button type="button" class="btn btn-danger">Failed - system problem '+JSON.stringify(errorThrown)+' please contact support desk</button>');
}

function ajaxPOST(url,jsonStr,funcSuccess){
	return $.ajax({
	    type: 'POST',
	    url: url,//+'&'+csrfname+'='+csrfvalue,
	    data: jsonStr,
	    contentType: "application/json",
	    dataType: 'json',
	    success : funcSuccess
	});
}

   function ValidateEmail(inputText){  
    	var mailformat = prefs.storeparams.emailValidation; ///^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;  
    	if(inputText.match(mailformat))  
    	{  
    		return true;  
    	}else{  		   
		    return false;  
    	}  
    }

	function phonenumber(inputtxt){  
      var phoneno = prefs.storeparams.phoneValidation;// /^\d{10,15}$/;  
      if (inputtxt.match(phoneno)){
    	  
          return true;  
       }else{    
          return false;  
     	}  
    }  
    
	function getInputs(attrib){
		var res = []
		var match;
		var key;
		for (var i = 0, len = attrib.length; i < len; i++) {
			key = attrib[i];			
			match = document.getElementById( key ).value;
			res.push(match)			
		}		
		return res
	}

	
/*
 * fields: ['fname','lname' etc ]
 * returns postObject with field values and errorcount
 */
	function preValidateFields(fields){
		var postObject = {errorcount:0}		
		fields.forEach( function( fieldItem ){
			var widget = $('#'+fieldItem );
			var widgetval = $('#'+fieldItem ).val().toString();
			widgetval = widgetval!=undefined ? widgetval.trim() : "";
			//console.log("widget "+fieldItem+"=>"+widgetval);
			var fieldError = false;			
			switch (fieldItem) {
				case 'uid' : postObject.uid = parseInt(widgetval); break;
				case 'fname': postObject.fname = widgetval;
							  if (widgetval.length < 2) fieldError = true;
							  break; // fname
				case 'lname': postObject.lname = widgetval; 
							  if (widgetval.length < 2) fieldError = true;
							  break;
				break; // fname
				case 'email': postObject.email = widgetval; 
							  if ((widgetval!=NO_EMAIL_CALL)&&(!ValidateEmail(widgetval))) fieldError = true;
							  break; // email;
				case 'phone': postObject.phone = widgetval; 
							  if (!phonenumber(widgetval)) fieldError = true;
							  break; //phone
				case 'addr1': postObject.addr1 = widgetval; 
							  break; //addr1
				case 'postcode' : postObject.postcode = widgetval; 
							break; //postcode							
				case 'emailupdates' : postObject.msg_updates = (widget.is(':checked')); 
							break; //updates
				case 'emailmailshot' : postObject.msg_mailshot = (widget.is(':checked')); 
							break; //mailshot
							
			}//case	
			if (fieldError){
				// mark in red	
				console.log( fieldItem+' has problem ' + widgetval)
				widget.addClass("is-invalid") //background-color:#fdb9bc;
				widget.removeClass("is-valid");
				postObject.errorcount++;
			}else{
				widget.removeClass("is-invalid");
				widget.addClass("is-valid")
			}
		});
		
		return postObject;					
	}
	
	/*
	 * fields: ['fname','lname' etc ]
	 * data: [cust1,cust2]
	 * */
	function highlightFieldDiff( fields , data ){
   		fields.forEach( function( fieldItem ){
   			var widget = $('#'+fieldItem );
   			var widgetval = $('#'+fieldItem ).val().toString();
   			widgetval = widgetval!=undefined ? widgetval.trim() : "";    			
   			data.forEach( function( excust ){
   				var highlight = false;
       			switch (fieldItem) {
						case 'email': highlight = widgetval==excust.email; break; 
						case 'phone': highlight = widgetval==excust.phone; break;
   				}
   				console.log(fieldItem+" same "+highlight)
   				if (highlight){
   					// mark in red	    				
   					widget.addClass("is-invalid") //background-color:#fdb9bc;
   					widget.removeClass("is-valid");    				
   				}else{
   					widget.removeClass("is-invalid");
   					widget.addClass("is-valid")
   				}    				
   			});
   			
   		});    			
   }
