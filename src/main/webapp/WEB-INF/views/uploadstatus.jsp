
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp" />

<jsp:include page="menu.jsp" />

<script type="text/javascript">
	var csrfname = "${_csrf.parameterName}";
	var csrfvalue = "${_csrf.token}";

	function pageStart() {
		startUpdate();
	}

	 function startUpdate(){		 
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

			<div class="modal-header">
				<b>Import status</b>
			</div>

			<div class="modal-body">
				<div id="updatetext"></div>
				<div class="container-fluid" align="right">
					<button class="btn btn-success" 
						id='updatecomplete'>Complete</button>
				</div>
			</div>

</body>
</html>
