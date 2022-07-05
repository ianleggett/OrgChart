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