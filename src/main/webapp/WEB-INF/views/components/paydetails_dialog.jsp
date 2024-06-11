<div id="editpaydetails" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" >  
    <!-- Modal content-->
    <div class="modal-content">
       <div class="modal-header"> <h2>Pay Details</h2>     
       <button type="button" class="close" data-dismiss="modal">&times;</button>
       </div>
       <div class="modal-body">      		
       <form id="pay_form">
       <input type="hidden" id="payid" value="0">
       <table class="table-striped table-bordered" width="100%">			
		<tr>
			<td align="right"><div class="col">Method <i style="color:red;">*</i></div></td>			
			<td><div class="col">	
					 <input type="text" disabled class="form-control" id="payName"
							autocomplete="off">			 
				</div> 
			</td>
			<tr>		
			<tr>
       		<td align="right"><div class="col">Details</div></td><td>
       		<div class="col">
       		</div>
       		<div class="col">
       			<textarea class="form-control" id="payDetails" ></textarea>       			
       		</div>       			
       		</td> 
       		</tr><tr>
			<td>					
       		 </td><td align="right">			
					<div class="col">
       		 			<button onclick="addpaymethod()" type="button" class="btn btn-success">OK</button>
       		 		</div>
       		 </td></tr>      		
       		</table>	
       		</form> 
	   </div>      
      </div>     
    </div>
</div>