
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp" />

<jsp:include page="menu.jsp" />

<script type="text/javascript">
	var csrfname = "${_csrf.parameterName}";
	var csrfvalue = "${_csrf.token}";

	function pageStart() {

		$.getJSON('viewdata.json', function(vdata) {
			$('#view').empty();
			vdata.data.forEach(function(item){
				$('#view').append('<option>'+item.name+'</option>');				
			});			
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
</script>

<div id='topstrip' class="alert alert-primary"><table width="100%"><tr><td align="center">Import Team/Group data</td></tr></table></div>


<div class="accordion-item">
	<div class="accordion-body">
		<form id="uploadForm" method="POST" action="importgroup?${_csrf.parameterName}=${_csrf.token}"
			enctype="multipart/form-data">	
			<table class="table table-striped table-bordered" cellspacing="0" width="90%"><tr>
			<td>Import to view</td><td>	
			 <select id="view" name="view">
  			</select></td></tr>
  			<tr>
  			<td>File</td><td>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" /><input type="file"
				name="file"> <input type="submit" value="Upload">
				</td></tr>
				</table> 				
		</form>
	</div>
</div>

</body>
</html>
