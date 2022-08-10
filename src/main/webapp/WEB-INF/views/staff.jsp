
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp" />

<jsp:include page="menu.jsp" />

<script type="text/javascript">
	var csrfname = "${_csrf.parameterName}";
	var csrfvalue = "${_csrf.token}";
	var contData;
	
	var tempDept;
	var tempGroup;
	var table;
	
	function setDepList(value, key, map) {
		  console.log(`m[${key}] = ${value}`);
	}
	
	function refreshView(){
		 //location.reload();
		 table.ajax.reload();
	}
	
	function resetJobs(targetcombo){
		var tcombo = "#"+targetcombo;
		$( tcombo ).empty();
		$( "<option>" ).attr("value","0").html("=select=").appendTo( tcombo );	
	}
	
	function setContainerItems(grplst,targetcombo){
		var tcombo = "#"+targetcombo;		
		$( tcombo ).empty();
		$( "<option>" ).attr("value","0").html("=select=").appendTo( tcombo );	
		$.each( grplst, function( i, item ) {		
			$(tcombo).append('<option>'+item+'</option>');
		 });		
	}
	
	function setUpJobs(){		
		
		setContainerItems(Object.keys(contData),"deptName");	
		
		$('#deptName').change(function(e) {			
		    var val = $('#deptName').val();		  
			resetJobs('groupName');
			resetJobs('teamName'); 
			tempDept = val;
			setContainerItems(Object.keys(contData[tempDept]),"groupName");
		});

		$('#groupName').change(function(e) {		
		    var val = $('#groupName').val();
		    resetJobs('teamName'); 
		    tempGroup = val;
		    console.log("groupChange "+val)
		    console.log(JSON.stringify(contData[tempDept][tempGroup]))
		    setContainerItems(contData[tempDept][tempGroup],"teamName");
		});
		
		$('#teamName').change(function(e) {		
		    var val = $('#teamName').val();
		    
		});
	}
	
	function populateTable( inum ){
		$('#inum').val(inum);
		$('#cid').val();
		//$('#deptName').val(container.deptName);
		//$('#groupName').val(container.groupName);
		//$('#teamName').val(container.teamName);				
	}
	
	function showContainerEdit(inum) {
		//console.log(JSON.stringify(data));
		populateTable(inum)
		$('#editCode').modal('show');		
	}
	function cancel() {
		$('#editCode').modal('hide');		
	}
	function doChanges(){		
		var attrib = {inum:'nocheck', descr: 'nocheck',deptName:'string', groupName:'string', teamName:'string'}		
		var postObject = validateForm(attrib);	
		
		const URL_TARGET = 'updateStaff.json';
				
		postObject.descr = '${view}';
		
		console.log(JSON.stringify(postObject));	
			
		if (postObject.errorcount==0){	
			$.ajax({
			    type: 'POST',
			    url: URL_TARGET + '?'+csrfname+'='+csrfvalue,
			    data: JSON.stringify(postObject),
			    contentType: "application/json",
			    dataType: 'json',
			    success: function(data) {				    	
			    	if (data.statusCode==0){		    		
			    		refreshView();
			    		$('#editCode').modal('hide');
			    	}else{		    		
			    		$('#editCode').modal('hide');
				    	$('#msgdialog').modal('show');
				    	$('#msgtitle').html('CCY updated');
			    		$('#msgdetails').html('<button type="button" class="btn btn-danger">Failed '+data.msg+'</button>');		    		
			    	}	
			    },	
			    fail: function(jqXHR, textStatus, errorThrown) {
			    	$('#editjob').modal('hide');
			    	$('#msgdetails').html('<button type="button" class="btn btn-danger">Failed - system is down please contact support desk</button>');
			    },
			});		
	    }	
	}
	
	
	function pageStart() {

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

		$.getJSON('containerAggData.json?v=${view}', function(data) {								
			contData = data.strMapList;
			setUpJobs();
		});	
		
	}	

	function format(d) {
	    // `d` is the original data object for the row
	    return (
	        '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' +
	        '<tr>' +
	        '<td>started:</td>' +
	        '<td>' +
	        d.started +
	        '</td>' +
	        '</tr>' +
	        '<tr>' +
	        '<td>email:</td>' +
	        '<td>' +
	        d.email +
	        '</td>' +
	        '</tr>' +
	        '<tr>' +
	        '<td>descr:</td>' +
	        '<td>' +
	        d.descr +
	        '</td>' +
	        '</tr>' +
	        '<tr>' +
	        '</table>'
	    );
	}
	
	function deptTeamRender(data, type, row, meta){
		//console.log(JSON.stringify(row.inum));
		return '<a href="#" onclick="showContainerEdit(\''
		+ row.inum
		+ '\',\''+row.containerId+'\')" >'														
		+ data + '</a>';
	}
	
	function setTable(tabledata) {

		table = $('#example')
				.DataTable(
						{
							processing : true,
							language : {
								processing : '<i class="fa fa-spinner fa-spin fa-3x fa-fw"></i><span class="sr-only">Loading...</span> '
							},
							ajax : 'staffdata.json?v=${view}',
							searching : true,
							scrollY: '500px',
					        scrollCollapse: true,
					        paging: false,
							info : true,
							columns : [	
								{
					                className: 'dt-control',
					                orderable: false,
					                data: null,
					                defaultContent: '',
					            },
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
										render : deptTeamRender
									}, {
										data : "groupName",
										render : deptTeamRender
									}, {
										data : "teamName",
										render : deptTeamRender
									}, {
										data : "city"
									}, {
										data : "jobCat"
									}, {
										data : "jobTitle"
									}, {
										data : "geoReg"
									} 
									],
							"order" : [ [ 3, 'asc' ] ]
						});
		
		   // Add event listener for opening and closing details
	    $('#example tbody').on('click', 'td.dt-control', function () {
	        var tr = $(this).closest('tr');
	        var row = table.row(tr);
	 
	        if (row.child.isShown()) {
	            // This row is already open - close it
	            row.child.hide();
	            tr.removeClass('shown');
	        } else {
	            // Open this row
	            row.child(format(row.data())).show();
	            tr.addClass('shown');
	        }
	    });

	}

	function createSelect(id) {

		$("#dropdown").append($("<option>", {
			response : response.category[i].name,
			text : response.category[i].name
		}));
	}
</script>

<table id="example" class="table table-striped table-bordered">
	<thead>
		<tr>			
		<th>a</th>	  
			<th>inum</th>
			<th>firstName</th>
			<th>lastName</th>
			<th>Dept</th>
			<th>Grp</th>
			<th>Team</th>
			<th>city</th>			
			<th>jobCat</th>
			<th>jobTitle</th>			
			<th>geoReg</th>			
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

<div id="editCode" class="modal fade" role="dialog">
  <div class="modal-dialog">
<form action="updateContainer" method="POST">
    <!-- Modal content-->
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input type="hidden" id='inum' name="inum" value="not-set" /> 
    <div class="modal-content">
      <div class="modal-header">      
        <button type="button" class="btn btn-danger" onclick="cancel()" data-dismiss="modal">&times; Cancel</button>
        <button type="button" class="btn btn-success" onclick="doChanges()">Update</button>         
      </div>
     <div class="modal-body">
     	 <table class="table-striped table-bordered">
     	<tr>		
			<td align="right"><div class="col">Dept</div></td>
			<td> <div class="col">
			 <select id="deptName" class="autocomplete">
  			</select>
			</div></td>								
		</tr>
		<tr>		
			<td align="right"><div class="col">Group</div></td>
			<td> <div class="col">
			 <select id="groupName" class="autocomplete">
  			</select>				
			</div></td>	
		</tr>
		<tr>		
			<td align="right"><div class="col">Team</div></td>
			<td> <div class="col">			
			<select id="teamName" class="autocomplete">
  			</select>
			</div></td>	
		</tr>
		</table>    			
		
        <table id="statustable"></table>
      </div>     
    </div>
</form>
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
