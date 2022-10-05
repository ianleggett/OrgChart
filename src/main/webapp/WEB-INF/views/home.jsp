
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp" />

<jsp:include page="menu.jsp" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>

<script type="text/javascript">
	var csrfname = "${_csrf.parameterName}";
	var csrfvalue = "${_csrf.token}";
    const DATE_FMT = "YYYY-MMM-DD HH:mm";
    
	function pageStart() {		
		setUptable();
		
	}

	function setUptable(){
	table = $('#lastupdate')
	.DataTable(
			{
				processing : true,
				language : {
					processing : '<i class="fa fa-spinner fa-spin fa-3x fa-fw"></i><span class="sr-only">Loading...</span> '
				},
				ajax : 'importlog.json',
				searching : false,				
		        scrollCollapse: true,
		        paging: false,
				info : false,
				columns : [						
						{
							data : "timestamp",
							render: function(data, type, row){				                
				                return moment(data).format(DATE_FMT);
				            }
						},
						{
							data : "notes"
						}
						],
				"order" : [ [ 0, 'desc' ] ]
			});
	
	table2 = $('#staffchanges')
	.DataTable(
			{
				processing : true,
				language : {
					processing : '<i class="fa fa-spinner fa-spin fa-3x fa-fw"></i><span class="sr-only">Loading...</span> '
				},
				ajax : 'updatelog.json',
				searching : false,				
		        scrollCollapse: true,
		        paging: false,
				info : false,
				columns : [						
						{
							data : "timestamp",
							render: function(data, type, row){				                
				                return moment(data).format(DATE_FMT);
				            }
						},						
						{
							data : "refId"
						},
						{
							data : "notes"
						},
						{
							data : "updateType"
						}
						
						],
				"order" : [ [ 0, 'desc' ] ]
			});
	
	table2 = $('#staffmoves')
	.DataTable(
			{
				processing : true,
				language : {
					processing : '<i class="fa fa-spinner fa-spin fa-3x fa-fw"></i><span class="sr-only">Loading...</span> '
				},
				ajax : 'changelog.json',
				searching : false,				
		        scrollCollapse: true,
		        paging: false,
				info : false,
				columns : [						
						{
							data : "timestamp",
							render: function(data, type, row){				                
				                return moment(data).format(DATE_FMT);
				            }
						},
						{
							data : "notes"
						},												
						{
							data : "updateType"
						},
						{
							data : "refId"
						}
						],
				"order" : [ [ 0, 'desc' ] ]
			});
	
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
    <h5 class="card-title">Recent Imports</h5>
    <table id="lastupdate" class="table table-striped table-bordered">
	<thead>
		<tr>					
			<th>Time</th>
			<th>Notes</th>
		</tr>
	</thead>
	</table>  
	<br/>
		<h5 class="card-title">Team Moves</h5>  
	<table id="staffmoves" class="table table-striped table-bordered">
    	<thead>
		<tr>					
			<th>Time</th>			
			<th>Name</th>
			<th>Change</th>
			<th>Team</th>
		</tr>
	</thead>
	</table> 
	<br/>	
	<h5 class="card-title">Staff Add/Removes</h5>  
	<table id="staffchanges" class="table table-striped table-bordered">
    	<thead>
		<tr>					
			<th>Time</th>			
			<th>iNum</th>
			<th>Name</th>
			<th>Change</th>
		</tr>
	</thead>
	</table> 
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
