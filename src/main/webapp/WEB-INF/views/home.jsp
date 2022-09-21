
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

<div class="card">
  <div class="card-body">
   
  </div>
</div>

<div class="card">
  <div class="card-body">
    <h5 class="card-title">OrgView demo site</h5>
    <h6 class="card-subtitle mb-2 text-muted">Notes & Information</h6>
    <p class="card-text">Last update : yyyy-mm-dd</p>
    <p class="card-text">Members : ?? added, ?? removed</p>
  </div>
</div>

<div class="card">
  <div class="card-body">
   
  </div>
</div>

<div class="accordion" id="accordionExample">
  <div class="accordion-item">
    <h2 class="accordion-header" id="headingOne">
      <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="false" aria-controls="collapseOne">
        System details
      </button>
    </h2>
    <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#accordionExample">
      <div class="accordion-body">
      </div>
    </div>
  </div>

 
</div>



</div>

</body>
</html>
