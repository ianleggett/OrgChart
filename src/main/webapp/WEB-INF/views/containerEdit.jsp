<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
	#scrollable-dropdown-menu .tt-dropdown-menu {
  max-height: 150px;
  overflow-y: auto;
}

td.details-edit {
    background: url('resources/image/edit.png') no-repeat center center;
    cursor: pointer;
}
td.details-orders {
    background: url('resources/image/orders.png') no-repeat center center;
    cursor: pointer;
}

 </style>


<jsp:include page="header.jsp" />

<script type="text/javascript">

var csrfname="${_csrf.parameterName}";
var csrfvalue="${_csrf.token}";
var table;
var contData;
var contMap={};
//var ordertable;
var prefs;

function pageStart(){

	$.getJSON('containerdata.json', function(data) {		
		contData = data;
		//console.log(JSON.stringify(contData));
		contData.data.forEach(function(item){
			contMap[item.id] = item;			
		});
		initTable();
	});	
}

function range(start, end) {
    return (new Array(end - start + 1)).fill(undefined).map((_, i) => i + start);
}

function populateTable( container ){
	
	$('#cid').val(container.id);
	$('#deptName').val(container.deptName);
	$('#groupName').val(container.groupName);
	$('#teamName').val(container.teamName);		
	
}


function editTask(taskid){
	console.log(taskid);
	console.log( JSON.stringify(contMap[ taskid ]));
	$('#editCode').modal('show');
	populateTable( contMap[ taskid ] );
}
 
function doChanges(){
	
	var attrib = {cid:'nocheck',deptName:'string', groupName:'string', teamName:'string'}		
	var postObject = validateForm(attrib);	
	
	postObject.id = postObject.cid;
	delete postObject.cid;
	
	console.log(JSON.stringify(postObject));	
		
	if (postObject.errorcount==0){	
		$.ajax({
		    type: 'POST',
		    url: 'updateContainer.json'+'?'+csrfname+'='+csrfvalue,
		    data: JSON.stringify(postObject),
		    contentType: "application/json",
		    dataType: 'json',
		    success: function(data) {	
		    	$('#editCode').modal('hide');
		    	$('#msgdialog').modal('show');
		    	$('#msgtitle').html('CCY updated');
		    	if (data.statusCode==0){		    		
		    		$('#msgdetails').html('Success<button onclick="refreshView()" type="button" class="btn btn-success"><i class="fas fa-check"></i> OK</button>');
		    	}else{		    		
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
 
function initTable() {
	
    table = $('#example').DataTable( {
        data : contData.data,
        scrollCollapse : false,
        paging 		   : false,
        columns: [  
        	{ "data": null,
       		 	'render': function (dataIn){              
       		 	 return '<button class="btn btn-outline-dark" onclick="editTask('+dataIn.id+')"> <span class="fas fa-user-edit" data-toggle="tooltip" title="edit" style="color:grey;font-size:15px"></span></button>';
                  //return '<span onclick="editCust( \''+JSON.stringify(data)+'\' )" class="fas fa-user-edit" data-toggle="tooltip" title="edit"></span>';
                }
        	},        	
        	{ "data": "deptName" },
            { "data": "groupName" },
            { "data": "teamName" },           
        ],
        order: [[2, 'asc']]
    } );
    
    // Add event listener for opening and closing details
    $('#example tbody').on('click', 'td.details-edit', function () {    	
        var tr = $(this).closest('tr');
        var row = table.row( tr );
 		//editCust( row.data() );
    } );
    
    $('#example tbody').on('click', 'td.details-orders', function () {    	
        var tr = $(this).closest('tr');
        var row = table.row( tr );
        //showCustOrders( row.data() );
    } );
    

} 

</script>


<jsp:include page="menu.jsp" />
 
<div id='topstrip' class="alert alert-primary"><table width="100%"><tr><td align="center">Customers</td></tr></table></div>

<table id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th></th>  
                <th>dept</th> 
				<th>group</th>                            
                <th>team</th>                
            </tr>
        </thead>
       
    </table>

<div id="editCode" class="modal fade" role="dialog">
  <div class="modal-dialog">
<form action="updateContainer" method="POST">
    <!-- Modal content-->
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input type="hidden" id='cid' name="cid" value="not-set" /> 
    <div class="modal-content">
      <div class="modal-header">      
        <button type="button" class="btn btn-danger" data-dismiss="modal">&times; Cancel</button>
        <button type="button" class="btn btn-success" onclick="doChanges()">Update</button>         
      </div>
     <div class="modal-body">
     	 <table class="table-striped table-bordered">
     	<tr>		
			<td align="right"><div class="col">Dept</div></td>
			<td> <div class="col"><input type="text" class="form-control" id="deptName" placeholder="Dept"></div></td>								
		</tr>
		<tr>		
			<td align="right"><div class="col">Group</div></td>
			<td> <div class="col"><input type="text" class="form-control" id="groupName" placeholder="Group"></div></td>	
		</tr>
		<tr>		
			<td align="right"><div class="col">Team</div></td>
			<td> <div class="col"><input type="text" class="form-control" id="teamName" placeholder="Team"></div></td>	
		</tr>		

		</table>    			
		
        <table id="statustable"></table>
      </div>     
    </div>
</form>
  </div>
</div>



</body>
</html>
