
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp" />

<jsp:include page="menu.jsp" />

<script type="text/javascript">
	var csrfname = "${_csrf.parameterName}";
	var csrfvalue = "${_csrf.token}";

	function pageStart() {

		
	}

</script>
<div class="container">
<h2>OrgView demo site</h2>
<p>Sample ideas and proof of concepts</p>
<br/>
<p>* You can zoom diagrams using CTRL+mousewheel</p>
<p>* Click blue button to see the current view</p>
</div>

</body>
</html>
