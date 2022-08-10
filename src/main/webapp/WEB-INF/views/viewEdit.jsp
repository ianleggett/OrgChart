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

	$.getJSON('viewdata.json?v=${view}', function(data) {		
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
	
	$('#cid').val(1);
	$('#vname').val(container.name);
	$('#vdescr').val(container.descr);	
}

function addView(){
	$('#cid').val(0);
	$('#vname').val("");
	$('#vdescr').val("");
	$('#editCode').modal('show');	
}

function editView(taskid){
	console.log(taskid);
	console.log( JSON.stringify(contMap[ taskid ]));
	$('#editCode').modal('show');
	populateTable( contMap[ taskid ] );
}
 
function doChanges(){
	
	var attrib = {cid:'nocheck',vname:'string', vdescr:'string', readOnly:'boolean'}		
	var postObject = validateForm(attrib);	
	
	const URL_TARGET = postObject.cid == 0 ? 'addView.json' : 'updateView.json';
	
	postObject.id = postObject.cid;
	delete postObject.cid;
	postObject.name = postObject.vname;
	delete postObject.vname;
	postObject.descr = postObject.vdescr;
	delete postObject.vdescr;
	
	
	console.log(JSON.stringify(postObject));	
		
	if (postObject.errorcount==0){	
		$.ajax({
		    type: 'POST',
		    url: URL_TARGET + '?'+csrfname+'='+csrfvalue,
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
       		 	 return '<button class="btn btn-outline-dark" onclick="editView('+dataIn.id+')"> <span class="fas fa-user-edit" data-toggle="tooltip" title="edit" style="color:grey;font-size:15px"></span></button>';                  
                }
        	},        	
        	{ "data": "name" },
            { "data": "descr" },
            { "data": "readOnly" }           
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
 
<div id='topstrip' class="alert alert-primary"><table width="100%"><tr><td align="center">Views</td><td><button type="button" class="btn btn-success" onclick="addView()">add</button>  </td></tr></table></div>

<table id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th></th>  
                <th>name</th> 
				<th>description</th>                            
                <th>access</th>
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
			<td align="right"><div class="col">Name</div></td>
			<td> <div class="col"><input type="text" class="form-control" id="vname" placeholder="Name"></div></td>								
		</tr>
		<tr>		
			<td align="right"><div class="col">Description</div></td>
			<td> <div class="col"><input type="text" class="form-control" id="vdescr" placeholder="Description"></div></td>	
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
