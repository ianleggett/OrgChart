
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp" />

<script src="https://cdn.jsdelivr.net/npm/js-cookie@rc/dist/js.cookie.min.js"></script>
 <script src="https://cdn.jsdelivr.net/npm/mermaid/dist/mermaid.min.js"></script>

<jsp:include page="menu.jsp" />

<script type="text/javascript">
	var csrfname = "${_csrf.parameterName}";
	var csrfvalue = "${_csrf.token}";

	function initscript() {	
		 
		var config = { startOnLoad:true, 
			flowchart:{ useMaxWidth:false, htmlLabels:true, curve:'cardinal', },
			themeVariables: {
			    nodeBorder : "#004990",
			    mainBkg : "#c9d7e4",
			    nodeTextColor : "#274059",
			    fontFamily: "arial",
			    fontSize: "11px"
			 },
			 er:{
				 entityPadding: 5
			 },
			securityLevel:'loose',
			};		
		let insert = function (code) {
			$('#diag').html(code);
		  };
		const txt = "graph TD; A[Client] --> B[Load Balancer]; B --> C[Server01]; B --> D[Server02]";
		//pageStart(); // call others
		mermaid.initialize(config);
		
		$.getJSON('orgdata.json', function(dataIn){
			console.log(JSON.stringify(dataIn));
			mermaid.render("preparedScheme", dataIn.data, insert);
		}); 	
	}
		
</script>

   <div id="diag" class="mermaid flowchart">
    graph TD; A[loading...];
    </div>

</body>
</html>
