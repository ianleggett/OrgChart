<div id="editjob" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" >  
    <!-- Modal content-->
    <div class="modal-content">
       <div class="modal-header"> <h2>Offer Details</h2>     
       <button type="button" class="close" data-dismiss="modal">&times;</button>
       </div>
       <div class="modal-body">      		
       <form id="job_form">
       <input type="hidden" id="editid" value="0">
       <table class="table-striped table-bordered" width="100%">			
		<tr>
			<td align="right"><div class="col">From <i style="color:red;">*</i></div></td>			
			<td><div class="col">	
					  <select class="form-control" id="fromccyid"></select>					 
				</div> 
			</td>
			<tr>
			<td align="right"><div class="col">From Amount <i style="color:red;">*</i></div></td><td>
					<div class="col">
						<input type="text" class="form-control" id="fromAmount"
							placeholder="amount" autocomplete="off">
					</div>					
			</td>
			</tr>
			<tr>
			<td align="right"><div class="col">To <i style="color:red;">*</i></div></td>			
			<td><div class="col">	
					  <select class="form-control" id="toccyid"></select>					 
				</div> 
			</td>
			<tr>
			<td align="right"><div class="col">To Amount <i style="color:red;">*</i></div></td><td>
					<div class="col">
						<input type="text" class="form-control" id="toAmount"
							placeholder="amount" autocomplete="off">
					</div>					
			</td>
			</tr>
			
		    <tr>
			<td align="right"><div class="col">Time in Force<i style="color:red;">*</i></div></td><td>
					<div class="col">
						 <select class="form-control" id="tif">
						 <option value="GTC">GTC</option>
						 <option value="FOK">FOK</option>
						 </select>
					</div>					
			</td>
			</tr>
			<tr>
			<td align="right"><div class="col">Payment Methods<i style="color:red;">*</i></div></td>
			<td>
			    <div class="btn-group" role="group" aria-label="Basic checkbox toggle button group" id="paytypes">
			</div></td>
			</tr>			
			<tr>
       		<td align="right"><div class="col">Notes</div></td><td>
       		<div class="col" id="notediv">
       		</div>
       		<div class="col">
       			<textarea class="form-control" id="newlinenote" ></textarea>       			
       		</div>       			
       		</td> 
       		</tr><tr>
			<td>					
       		 </td><td align="right">			
					<div class="col">
       		 			<button onclick="placeOrder()" type="button" class="btn btn-success">OK</button>
       		 		</div>
       		 </td></tr>      		
       		</table>	
       		</form> 
	   </div>      
      </div>     
    </div>
</div>