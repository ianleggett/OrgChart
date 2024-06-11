<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<link rel="stylesheet" href="/resources/css/sceditor/default.min.css" />
<script src="/resources/js/sceditor/sceditor.min.js"></script>

<jsp:include page="header.jsp" />
<jsp:include page="adminpanel.jsp" />

<script src="/resources/js/sceditor/formats/bbcode.js"></script>
<script src="/resources/js/sceditor/formats/xhtml.js"></script>

<script src="/resources/js/jsoneditor.js"></script>


<script src="/resources/js/admin_schema.js"></script>

<script type="text/javascript">

const URL_PREFIX = '/system/';

const templateName = ['template_newuser','template_neworder','template_offeraccepted','template_deposit',
	'template_banksent','template_bankrec','template_tfrcomplete','template_footer'];

function restartNetwork(){
	 var csrfname="${_csrf.parameterName}";
  	 var csrfvalue="${_csrf.token}";
			
      // Get the value from the editor
      //console.log(JSON.stringify(editor.getValue()));
      var csrf_token = $('meta[name="csrf-token"]').attr('content');
      console.log('csrf_name:'+csrfname);
      console.log('csrf_value:'+csrfvalue);
      
      var request = $.ajax({
      	url: URL_PREFIX+'restartNetwork.json?'+csrfname+'='+csrfvalue,
      	type:"POST",
      	contentType : "application/json",
      	dataType : 'json',
      	data: JSON.stringify(""),        	
      	success: function(){
      		showMessage('Loaded OK',GREEN);
          },
      	error: function(){
      		showMessage('FAILED reconnect ',RED);
    	  }
      });
}


function reloadTradeSettings(){
	 var csrfname="${_csrf.parameterName}";
 	 var csrfvalue="${_csrf.token}";
			
     // Get the value from the editor
     //console.log(JSON.stringify(editor.getValue()));
     var csrf_token = $('meta[name="csrf-token"]').attr('content');
     console.log('csrf_name:'+csrfname);
     console.log('csrf_value:'+csrfvalue);
     
     var request = $.ajax({
     	url: URL_PREFIX+'reloadTradeSettings.json?'+csrfname+'='+csrfvalue,
     	type:"POST",
     	contentType : "application/json",
     	dataType : 'json',
     	data: JSON.stringify(""),        	
     	success: function(){
     		showMessage('Loaded OK',GREEN);
         },
     	error: function(){
     		showMessage('FAILED reconnect ',RED);
   	  }
     });
}


function shutdown(){
	
	if (confirm('Are you sure you want to shutdown?')){
	
	 var csrfname="${_csrf.parameterName}";
 	 var csrfvalue="${_csrf.token}";
			
     var csrf_token = $('meta[name="csrf-token"]').attr('content');
     console.log('csrf_name:'+csrfname);
     console.log('csrf_value:'+csrfvalue);
     
     var request = $.ajax({
     	url: URL_PREFIX+'shutdown?'+csrfname+'='+csrfvalue,
     	type:"POST",
     	contentType : "application/json",
     	dataType : 'json',
     	data: JSON.stringify(""),        	
     	success: function(){
     		alert('Site has been shutdown');
         },
     	error: function(){
     		showMessage('FAILED to shutdown '+settingsText,RED);
   	  }
     });
     
     alert('Site has been shutdown');
     
	}
}

function setEditorData(schemaIn,holderId,starting_data){
	// Initialize the editor
	console.log("set editor "+holderId)
	var editor = new JSONEditor(document.getElementById(holderId),{
	  // Enable fetching schemas via ajax
	  theme: 'bootstrap3',
	  ajax: true,
	  schema: schemaIn,
	  
	  // Seed the form with a starting value
	  startval: starting_data,        
	  // Disable additional properties
	  no_additional_properties: true,
	  disable_collapse: true,
	  disable_edit_json: true,
	  disable_properties: true,
	  form_name_root: 'Test',
	  // Require all properties by default
	  required_by_default: true
	});
	
	return editor;
}


// 'adminemail.json','email_holder',email_schema,'save_email','adminsaveemail.json','Email Server settings'
function json_edit(jsonGet,holderId,schemaIn,saveButtonId,cancelButtonId,jsonSave,settingsText){
    var starting_email;// = [
    
    var ixy = $.getJSON(URL_PREFIX+jsonGet, function(data) {
  	  starting_email = data;
         
		    // Initialize the editor
		    var editor = setEditorData(schemaIn,holderId,data);
    
		    // Hook up the submit button to log to the console
		    document.getElementById(saveButtonId).addEventListener('click',function() {
		  	 
		  	 var csrfname="${_csrf.parameterName}";
		  	 var csrfvalue="${_csrf.token}";
					
		      // Get the value from the editor
		      //console.log(JSON.stringify(editor.getValue()));
		      var csrf_token = $('meta[name="csrf-token"]').attr('content');
		      console.log('csrf_name:'+csrfname);
		      console.log('csrf_value:'+csrfvalue);
		      
		      var request = $.ajax({
		      	url: URL_PREFIX+jsonSave+'?'+csrfname+'='+csrfvalue,
		      	type:"POST",
		      	contentType : "application/json",
		      	dataType : 'json',
		      	data: JSON.stringify(editor.getValue()),        	
		      	success: function(){
		      		showMessage('Saved '+settingsText,GREEN);
		      		editor.destroy();
		      		ixy = $.getJSON(jsonGet, function(data) {		      	  	  
		      			editor = setEditorData(schemaIn,holderId,data);
		      		});	      		
		          },
		      	error: function(){
		      		showMessage('FAILED to save '+settingsText,RED);
		    	  }
		      });
		    	  		
		   
		    });
    
		    // Hook up the Restore to Default button
		    document.getElementById(cancelButtonId).addEventListener('click',function() {
		      editor.setValue(starting_email);
		    });
    

		    // Hook up the validation indicator to update its 
		    // status whenever the editor changes
		    editor.on('change',function() {
		      // Get an array of errors from the validator
		      var errors = editor.validate();
		      
		      var savebutt = document.getElementById(saveButtonId);        
		      // Not valid
		      if(errors.length) {
		        savebutt.setAttribute('class',"btn btn-danger")
		        savebutt.setAttribute('disabled','')
		      }
		      // Valid
		      else {        
		        savebutt.removeAttribute('disabled')
		        savebutt.setAttribute('class',"btn btn-success")
		      }
		    });
    
	  });  // JSON call
}

 json_edit('admincryptsettings.json','crypto_holder',crypto_schema,'save_crypto','cancel_crypto','admincryptsave.json','Crypto settings');
 json_edit('adminkeyssettings.json','keys_holder',keys_schema,'save_keys','cancel_keys','adminkeyssave.json','Private Keys');
 json_edit('admintradesettings.json','trade_holder',trade_schema,'save_trade','cancel_trade','admintradesave.json','Trade settings');
 //json_edit('adminvenuesettings.json','venue_holder',venue_schema,'save_venue','cancel_venue','adminvenuesave.json','Venue / exchange settings');
 json_edit('adminemailsettings.json','email_holder',email_schema,'save_email','cancel_email','adminemailsave.json','Email settings');
 json_edit('adminstaffusers.json','staff_holder',staff_schema,'save_staff','cancel_staff','adminsavestaff.json','Staff User settings');
 //json_edit('adminsymsettings.json','sym_holder',sym_schema,'save_sym','cancel_sym','adminsymsave.json','Symbol settings');
 
 function changeNet(radio) {
	 
	 console.log("change network "+radio.value);
	 
	 ajaxPOST('/system/setnetworkprofile.json?profile='+radio.value,"{}",function(resp){
		 console.log(resp)
		 location.reload();   	
	 });  	 
	    
	}
 
 function updateStatus(){
	 var status = { deposit: true, sendFiat: false, releaseFund : false};
	status.deposit = $("#stat_deposit").is(':checked');
	status.sendFiat = $("#stat_fiat").is(':checked');
	status.releaseFund = $("#stat_release").is(':checked');
	console.log(JSON.stringify(status));
	ajaxPOST('/system/setsystem.json',JSON.stringify(status),function(resp){
		 console.log(resp)
		// location.reload();
	}); 
	var msg = $("#stat_msg").val();
	if ( msg.length != 0){		
		console.log(msg)
		var sysmsg = {msgid:0,msg:msg};
		ajaxPOST('/system/setsystemmsg.json',JSON.stringify(sysmsg),function(resp){
			
			// location.reload();
		}); 
	}	
 }

 function getStatus(){
	 $.getJSON('/system/getsystem.json', function(systemstatus){				 
		 $('#stat_deposit').prop("checked",systemstatus.deposit);
		 $('#stat_fiat').prop("checked",systemstatus.sendFiat);
		 $('#stat_release').prop("checked",systemstatus.releaseFund);
	 });
	 	 	 
	 $.getJSON('/system/getsystemmsg.json', function(systemmsg){	
		 console.log(systemmsg)
		 $('#stat_msg').val(systemmsg.msg);		 
	 });
	 
 }
 
 
 function pageStart(){
	 // get network	 	 
	 $.getJSON('/v1/public/getnetworkprofile.json', function(netname){		 
		 console.log("netname "+netname.value);
			$('#opt_'+netname.value).prop("checked",true);
			/* email templates not done here anymore			
			templateName.forEach( function(item){
				console.log("getting "+item);
				populateTemplate(item);
			});
			*/
			
			getStatus();
			showMessage('loaded settings OK',GREEN);	 
			
		}); 	 
 }
 
 function startUpdate(){	 
	 ajaxPOST('updateCatalog.json?',"",function(resp){			
	    	
		});
	 $('#updatetext').empty();
	 $('#updatecomplete').addClass('invisible');
	 $('#updateModal').modal('show');
	 var timer = setInterval(function(){ 
			$.getJSON('updateStatus.json', function(data){				
				$('#updatetext').html(data.webtext);
				console.log(JSON.stringify(data));
				if (data.complete){					
					clearInterval(timer);
					$('#updatecomplete').removeClass('invisible');
				}
			}); 	
		 		 
	 }, 1000);	 
 }
 
 function hideupdates(){
	 $('#updateModal').modal('hide');
	 location.reload();
 }
 
 function showSaved(){
	 $('#saved').modal('show');
 }
    </script>

<div class="card">
  <div class="card-header">
    Settings
  </div>
  <div class="card-body">
 
	<div class="accordion accordion-flush" id="accordionFlushExample">
	
	<div class="card">
	  <div class="card-header">
	    System Status
	  </div>
				<div class="card-body">
					<span class="float-left">
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" id="stat_deposit"
								value="option1"> <label class="form-check-label"
								for="stat_deposit">Allow Deposit</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" id="stat_fiat"
								value="option2"> <label class="form-check-label"
								for="stat_fiat">Allow Fiat</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" id="stat_release"
								value="option3"> <label class="form-check-label" for=""stat_release"">Allow
								release</label>
						</div>
						</span>
						<span class="float-right">
							<input type="text" class="form-control" id="stat_msg" aria-label="Default"
								aria-describedby="inputGroup-sizing-default">													
						</span>					
						<button class="btn btn-sm btn-warning" onclick="updateStatus()">update</button>
				</div>
	</div>
	<div class="card">
	  <div class="card-header">
	    Network
	  </div>
	  <div class="card-body">
	    	<span class="float-left">
					<div class="form-check form-check-inline">
					  <input class="form-check-input" type="radio" name="inlineRadioOptions" onclick="changeNet(this);" id="opt_local" value="local">
					  <label class="form-check-label" for="inlineRadio1">Local</label>
					</div>
					<div class="form-check form-check-inline">
					  <input class="form-check-input" type="radio" name="inlineRadioOptions" onclick="changeNet(this);" id="opt_kovan" value="kovan">
					  <label class="form-check-label" for="inlineRadio2">Kovan</label>
					</div>
					<div class="form-check form-check-inline">
					  <input class="form-check-input" type="radio" name="inlineRadioOptions" onclick="changeNet(this);" id="opt_rinkeby" value="rinkeby">
					  <label class="form-check-label" for="inlineRadio3">Rinkeby</label>
					</div>
					<div class="form-check form-check-inline">
					  <input class="form-check-input" type="radio" name="inlineRadioOptions" onclick="changeNet(this);" id="opt_ropsten" value="ropsten">
					  <label class="form-check-label" for="inlineRadio3">Ropsten</label>
					</div>	
					<div class="form-check form-check-inline">
					  <input class="form-check-input" type="radio" name="inlineRadioOptions" onclick="changeNet(this);" id="opt_mainnet" value="mainnet">
					  <label class="form-check-label" for="inlineRadio3">MainNet</label>
					</div>										
					</span>
					<span class="float-right">
						<button class="btn btn-sm btn-warning" onclick="restartNetwork()">Restart Network</button>	
						<button class="btn btn-sm btn-warning" onclick="deployEscrow()">Deploy Escrow Contract</button>
						<button class="btn btn-sm btn-danger" disabled onclick="shutdown()">Shutdown</button>
					</span>	
	  </div>
	  
	  
	</div>
	
	  <!-- Crypto settings --->
	  <div class="accordion-item">
	    <h2 class="accordion-header" id="flush-heading0">
	      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapse0" aria-expanded="false" aria-controls="flush-collapse0">
	       Crypto Settings
	      </button>
	    </h2>
	    <div id="flush-collapse0" class="accordion-collapse collapse" aria-labelledby="flush-heading0" data-bs-parent="#accordionFlushExample">
	      <div class="accordion-body">
				<div id='crypto_holder' style="margin-left: 40px;"></div>
					<div class="container-fluid" style="margin-left: 40px;">
						<button id='cancel_crypto' class="btn btn-warning">Reset</button>
						<!-- button id='enable_disable'>Disable/Enable Form</button-->
						<button id='save_crypto'>Save</button>
					</div>
	      </div>
	    </div>
	  </div> <!-- End of Crypto settings --->
	
		  <!-- PVT KEYS settings --->
	  <div class="accordion-item">
	    <h2 class="accordion-header" id="flush-headingK">
	      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseK" aria-expanded="false" aria-controls="flush-collapseK">
	       Private Keys
	      </button>
	    </h2>
	    <div id="flush-collapseK" class="accordion-collapse collapse" aria-labelledby="flush-headingK" data-bs-parent="#accordionFlushExample">
	      <div class="accordion-body">
				<div id='keys_holder' style="margin-left: 40px;"></div>
					<div class="container-fluid" style="margin-left: 40px;">
						<button id='cancel_keys' class="btn btn-warning">Reset</button>
						<!-- button id='enable_disable'>Disable/Enable Form</button-->
						<button id='save_keys'>Save</button>
					</div>
	      </div>
	    </div>
	  </div> <!-- End of Crypto settings --->
	
	<!-- TRADE ACCOUNTS --->
	  <div class="accordion-item">
	    <h2 class="accordion-header" id="flush-heading1">
	      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapse1" aria-expanded="false" aria-controls="flush-collapse1">
	        Trade Accounts
	      </button>
	    </h2>
	    <div id="flush-collapse1" class="accordion-collapse collapse" aria-labelledby="flush-headingOne" data-bs-parent="#accordionFlushExample">
	      <div class="accordion-body">
	      <div id='staff_holder' ></div>
				<div class="container-fluid" style="margin-left: 40px;">
					<button id='cancel_staff' class="btn btn-warning">Reset</button>
					<!-- button id='enable_disable'>Disable/Enable Form</button-->
					<button id='save_staff' class="btn btn-success">Save</button>
				</div>
	      </div>
	    </div>
	  </div>  <!-- TRADE ACCOUNTS --->
	  
	  	  <!-- Email settings --->
	  <div class="accordion-item">
	    <h2 class="accordion-header" id="flush-headingEmail">
	      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseEmail" aria-expanded="false" aria-controls="flush-collapseEmail">
	       Email Settings
	      </button>
	    </h2>
	    <div id="flush-collapseEmail" class="accordion-collapse collapse" aria-labelledby="flush-headingEmail" data-bs-parent="#accordionFlushExample">
	      <div class="accordion-body">
				<div id='email_holder' style="margin-left: 40px;"></div>
					<div class="container-fluid" style="margin-left: 40px;">
						<button id='cancel_email' class="btn btn-warning">Reset</button>
						<!-- button id='enable_disable'>Disable/Enable Form</button-->
						<button id='save_email'>Save</button>
					</div>
	      </div>
	    </div>
	  </div> <!-- End of Crypto settings --->
	  
	  		<!-- Instruments --->
	  <div class="accordion-item">
	    <h2 class="accordion-header" id="flush-headingtrade">
	      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapsetrade" aria-expanded="false" aria-controls="flush-collapsetrade">
	       Trade Settings
	      </button>
	    </h2>
	    <div id="flush-collapsetrade" class="accordion-collapse collapse" aria-labelledby="flush-headingtrade" data-bs-parent="#accordionFlushExample">
	      <div class="accordion-body">
	     	 <button class="btn btn-sm btn-warning" onclick="reloadTradeSettings()">Reload settings</button>	
				<div id='trade_holder' style="margin-left: 40px;"></div>
					<div class="container-fluid" style="margin-left: 40px;">
						<button id='cancel_trade' class="btn btn-warning">Reset</button>
						<!-- button id='enable_disable'>Disable/Enable Form</button-->
						<button id='save_trade'>Save</button>
					</div>
	      </div>
	    </div>
	  </div> <!-- End of Instruments --->

	  <!-- System settings --->
	  <div class="accordion-item">
	    <h2 class="accordion-header" id="flush-heading4">
	      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapse4" aria-expanded="false" aria-controls="flush-collapse4">
	       System Settings
	      </button>
	    </h2>
	    <div id="flush-collapse4" class="accordion-collapse collapse" aria-labelledby="flush-heading4" data-bs-parent="#accordionFlushExample">
	      <div class="accordion-body">
				Logo <img alt="no logo" src="image?name=logo">
						<form method="POST"
							action="uploadfile?${_csrf.parameterName}=${_csrf.token}"
							enctype="multipart/form-data">
							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" /> File to upload: <input type="file"
								name="file"> <input type="submit" value="Upload">
							Press here to upload the file!
						</form>
	      </div>
	    </div>
	  </div> <!-- End of Instruments --->	 
	  
	</div>

  </div>
</div>


	<!--Accordion wrapper-->
	<div class="accordion md-accordion" id="accordionEx" role="tablist"
		aria-multiselectable="true">

		<!-- Accordion card -->
		<div class="card">

			<!-- Card header -->
			<div class="card-header" role="tab" id="headingOne1">
				<h5 class="mb-0">
	
				</h5>
			</div>

		</div>
	

			<!-- Edit Item Modal -->
			<div id="saved" class="modal fade" role="dialog">
				<div class="modal-dialog modal-sm">
					<!-- Modal content-->
					<div class="modal-content">
						<div class="modal-body">Your changes have been saved.</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default btn-theme"
								data-dismiss="modal">Close</button>
						</div>
					</div>
				</div>
			</div>

	<jsp:include page="components/msg_dialog.jsp" />

<jsp:include page="footer.jsp" />	
