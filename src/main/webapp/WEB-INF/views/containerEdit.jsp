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

	initView();
}

function range(start, end) {
    return (new Array(end - start + 1)).fill(undefined).map((_, i) => i + start);
}

function initView(){
	// location.reload();
	$.getJSON('containerdata.json?v=${view}', function(data) {		
		contData = data;
		//console.log(JSON.stringify(contData));
		contData.data.forEach(function(item){
			contMap[item.id] = item;			
		});
		initTable();
	});	
}

function refreshView(){
	$('#editCode').modal('hide');
	// location.reload();
	$.getJSON('containerdata.json?v=${view}', function(data) {		
		contData = data;
		//console.log(JSON.stringify(contData));
		contData.data.forEach(function(item){
			contMap[item.id] = item;			
		});
		table.clear();
		table.rows.add(contData.data).draw();
	});	
}

function hide(){
	$('#editCode').modal('hide');
}

function populateTable( container ){
	
	$('#cid').val(container.id);	
	$('#teamDesc').val(container.teamDesc);
	$('#teamName').val(container.teamName);		
	
}

function addContainer(){
	$('#cid').val(0);
	$('#teamDesc').val('');
	$('#teamName').val('');		
	$('#editCode').modal('show');
	
}

function editContainer(taskid){
	console.log(taskid);
	console.log( JSON.stringify(contMap[ taskid ]));
	$('#editCode').modal('show');
	populateTable( contMap[ taskid ] );
}
 
function doChanges(){
	
	var attrib = {cid:'nocheck', viewName: 'nocheck',teamDesc:'string', teamName:'string'}		
	var postObject = validateForm(attrib);	
	
	const URL_TARGET = postObject.cid == 0 ? 'addContainer.json' : 'updateContainer.json';
	
	postObject.id = postObject.cid;
	delete postObject.cid;
	postObject.viewName = '${view}';
	
	console.log(JSON.stringify(postObject));	
		
	if (postObject.errorcount==0){	
		$.ajax({
		    type: 'POST',
		    url: URL_TARGET + '?'+csrfname+'='+csrfvalue,
		    data: JSON.stringify(postObject),
		    contentType: "application/json",
		    dataType: 'json',
		    success: function(data) {
		    	console.log( JSON.stringify(data));		    			    
		    	if (data.statusCode==0){		   
		    		refreshView();	    		
		    	}else{		    		
		    		$('#editCode').modal('hide');
			    	$('#msgdialog').modal('show');
			    	$('#msgtitle').html('updated');
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
 
 
function deptTeamRender(data, type, row, meta){
	//console.log(JSON.stringify(row.inum));
	return '<a href="diag?v=${view}&d='+data+'" rel="noopener noreferrer" target="_blank" >' + data + '</a>';
}

function initTable() {
	
    table = $('#example').DataTable( {
        data : contData.data,          
        scrollCollapse : false,
        paging 		   : false,
        columns: [  
        	{ "data": null,
       		 	'render': function (dataIn){              
       		 	 return '<button class="btn btn-outline-dark" onclick="editContainer('+dataIn.id+')"> <span class="fas fa-user-edit" data-toggle="tooltip" title="edit" style="color:grey;font-size:15px"></span></button>';
                  //return '<span onclick="editCust( \''+JSON.stringify(data)+'\' )" class="fas fa-user-edit" data-toggle="tooltip" title="edit"></span>';
                }
        	},        	
        	{
				data : "teamName",
				render : deptTeamRender
			},
            { "data": "teamDesc" },  
            { "data": "count" },
        ],
        order: [[1, 'asc']]
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
 
<div id='topstrip' class="alert alert-primary"><table width="100%"><tr><td align="center">Teams & Groups</td><td><button type="button" class="btn btn-success" onclick="addContainer()">add</button>  </td></tr></table></div>

<table id="example" class="table table-striped table-bordered" cellspacing="0" width="90%">
        <thead>
            <tr>
                <th></th>  
                <th>Team</th> 
				<th>Description</th>                            
                <th>count</th>                
            </tr>
        </thead>
       
    </table>

<div id="editCode" class="modal fade bd-example-modal-lg" role="dialog">
  <div class="modal-dialog modal-lg">
<form action="updateContainer" method="POST">
    <!-- Modal content-->
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input type="hidden" id='cid' name="cid" value="not-set" /> 
    <div class="modal-content">
      <div class="modal-header">      
        <button type="button" class="btn btn-danger" onclick="hide()" data-dismiss="modal">&times; Cancel</button>
        <button type="button" class="btn btn-success" onclick="doChanges()">Update</button>         
      </div>
     <div class="modal-body">
     	 <table class="table table-striped table-bordered">
     	<tr>		
			<td align="right"><div class="col">Team</div></td>
			<td> <div class="col"><input type="text" class="form-control" id="teamName" placeholder="team name"></div></td>								
		</tr>
		<tr>		
			<td align="right"><div class="col">Description</div></td>
			<td> <div class="col"><input type="text" class="form-control" id="teamDesc" placeholder="description"></div></td>	
		</tr>		
		</table>    			        
      </div>     
    </div>
</form>
  </div>
</div>

<jsp:include page="components/msg_dialog.jsp" />

</body>
</html>
