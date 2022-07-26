
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp" />

<jsp:include page="menu.jsp" />

<script type="text/javascript">
	var csrfname = "${_csrf.parameterName}";
	var csrfvalue = "${_csrf.token}";

	function initscript() {

		var config = {
			startOnLoad : true,
			flowchart : {
				useMaxWidth : false,
				htmlLabels : true,
				curve : 'cardinal',
			},
			themeVariables : {
				nodeBorder : "#004990",
				mainBkg : "#c9d7e4",
				nodeTextColor : "#274059",
				fontFamily : "arial",
				fontSize : "11px"
			},
			er : {
				entityPadding : 5
			},
			securityLevel : 'loose',
		};
		let insert = function(code) {
			$('#diag').html(code);
		};
		const txt = "graph TD; A[Client] --> B[Load Balancer]; B --> C[Server01]; B --> D[Server02]";		

		setTable();

		//		$.getJSON('staffdata.json', function(dataIn){
		//			console.log(JSON.stringify(dataIn));
		//			setTable(dataIn);
		//		}); 		

		// $('#diag').text("graph TD; A[Client] --> B[Load Balancer]; B --> C[Server01]; B --> D[Server02]");		
	}

	function showStats() {
		$('#setItemModal').modal('show');
		//populateStatusTable( iid )
	}

	function setTable(tabledata) {

		table = $('#example')
				.DataTable(
						{
							processing : true,
							language : {
								processing : '<i class="fa fa-spinner fa-spin fa-3x fa-fw"></i><span class="sr-only">Loading...</span> '
							},
							ajax : 'staffdata.json',
							searching : true,
							paging : true,
							info : true,
							columns : [
									{
										data : "inum"
									},
									{
										data : "firstName"
									},
									{
										data : "lastName"
									},
									{
										data : "deptName",
										className : "text-center",
										render : function(data) {
											return '<button type="button" onclick="showStats(\''
													+ data.id
													+ '\')" class="btn btn-warning btn-xs">'
													+ '<i class="fas fa-edit"></i>'
													+ '</button>' + data;
											//createSelect(data);
											//return  "<select id='dropdown'>"+
											//        "<option>one</option>"+
											//        "<option>two</option>"+
											//        "</select>";
										}
									}, {
										data : "groupName"
									}, {
										data : "teamName"
									}, {
										data : "city"
									}, {
										data : "descr"
									}, {
										data : "jobCat"
									}, {
										data : "jobTitle"
									}, {
										data : "started"
									}, {
										data : "geoReg"
									}, {
										data : "email"
									}, ],
							"order" : [ [ 1, 'desc' ] ]
						});

	}

	function createSelect(id) {

		$("#dropdown").append($("<option>", {
			response : response.category[i].name,
			text : response.category[i].name
		}));
	}
</script>

<table id="example" class="table table-striped table-bordered"
	style="border-collapse: collapse; border-spacing: 0; width: 100%; border: solid 2px #0b0987;">
	<thead>
		<tr>
			<th>inum</th>
			<th>firstName</th>
			<th>lastName</th>
			<th>Dept</th>
			<th>Grp</th>
			<th>Team</th>
			<th>city</th>
			<th>descr</th>
			<th>jobCat</th>
			<th>jobTitle</th>
			<th>started</th>
			<th>geoReg</th>
			<th>email</th>
		</tr>
	</thead>
</table>

<div id="msgdialog" class="modal fade" id="rowtitle" role="alert">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<table width="100%">
					<tr>
						<td id="msgtitle">
					</tr>
				</table>
			</div>

			<div class="modal-body">
				<div id="msgdetails"></div>
			</div>
		</div>
	</div>
</div>

<div id="snackbar">Some text some message..</div>

<!-- Modal -->
<div id="setItemModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" style="color: white;"
					data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<table id="itemdetails" class="table table-striped table-bordered">
				</table>

			</div>
			<table id="statustable" class="table table-striped table-bordered"></table>
		</div>
	</div>

</div>


<!-- footer class="py-4 bg-light mt-auto">
					  <div class="container-fluid">
						  <div class="d-flex align-items-center justify-content-between small"> 
							  <div class="text-muted">(Version <spring:eval expression="@environment.getProperty('website.version')" />) Copyright &copy; Trade Finance tinance.com 2021</div>
							  <div>
								  <a href="#">Privacy Policy</a>
								  &middot;
								  <a href="#">Terms &amp; Conditions</a>
							  </div>
						  </div>
					  </div>
				  </footer>
				  -->

</body>
</html>
