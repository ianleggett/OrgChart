
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp" />

<jsp:include page="menu.jsp" />

<script type="text/javascript">
	var csrfname = "${_csrf.parameterName}";
	var csrfvalue = "${_csrf.token}";

	function pageStart() {

	}
/**
	function uploadStaff(){
		console.log("Uploading");
		var fd = new FormData();
        var files = $('#file')[0].files[0];
        fd.append('file', files);
        
		jQuery.ajax({
            url: "importstaff?${_csrf.parameterName}=${_csrf.token}",
            type: "POST",
            contentType:attr( "enctype", "multipart/form-data" ),
            data: fd,
            processData: false,
            contentType: false,
            success: function (res) {
            	alert(res);
             //jQuery('div#response').html("Successfully uploaded");
            },
		 	fail: function(jqXHR, textStatus, errorThrown) {		    	
		 		alert("Fail " + textStatus);
		    },
        });
		**/
		/**
		$.ajax({
		    type: 'POST',
		    url: URL_TARGET + '?'+csrfname+'='+csrfvalue,
		    data: JSON.stringify(postObject),
		    contentType: "multipart/form-data",
		    dataType: 'json',
		    success: function(data) {
		    	console.log( JSON.stringify(data));		    			    
		    	if (data.statusCode==0){		   
		    		refreshView();	    		
		    	}else{		    		
		    		$('#msgdetails').html('<button type="button" class="btn btn-danger">Failed '+data.msg+'</button>');		    		
		    	}	
		    },	
		    fail: function(jqXHR, textStatus, errorThrown) {
		    	$('#editjob').modal('hide');
		    	$('#msgdetails').html('<button type="button" class="btn btn-danger">Failed - system is down please contact support desk</button>');
		    },
		});	
		
		****/
	
	
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
</script>

<div id='topstrip' class="alert alert-primary"><table width="100%"><tr><td align="center">Import Staff data</td></tr></table></div>


<div class="accordion-item">
	<div class="accordion-body">
		<form id="uploadForm" method="POST" action="importstaff?${_csrf.parameterName}=${_csrf.token}"
			enctype="multipart/form-data">			
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" /> File to upload: <input type="file"
				name="file"> <input type="submit" value="Upload"				> 				
		</form>
	</div>
</div>

</body>
</html>
