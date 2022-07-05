<div id="cust_dlg" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form id='custform' action="" method="POST">
	    <!-- Modal content-->
		    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
 			<input type="hidden" id='cid' name="cid" value="0" /> 
			<div class="modal-content">
				<div class="modal-header bg-primary"> 
					<h2>Customer Details</h2>     
        			<button type="button" class="close" data-dismiss="modal">&times;</button>        
      			</div>
     			<div class="modal-body">
					<table  class="table table-active table-sm">
						<tr>
							<td><label for="name">Name</label></td>
							<td><input type="text" class="form-control" name="name" id="name" placeholder="Name"></td>
						</tr>
						<tr>
							<td><label for="email">Email</label></td>
							<td><input type="email" class="form-control" name="email" id="email" placeholder="Enter Email"></td>
						</tr>
						<tr>
							<td><label for="phone">Phone</label></td>
							<td><input type="tel" class="form-control" name="phone" id="phone" placeholder="Enter Phone Number"></td>
						</tr>
						<tr>
							<td><label for="bankName">Bank Name</label></td>
							<td><input type="text" class="form-control" name="bankName" id="bankName" placeholder="Bank Name"></td>
						</tr>
						<tr>
							<td><label for="accName">Account Name</label></td>
							<td><input type="text" class="form-control" name=accName id="accName" placeholder="Account Name"></td>
						</tr>
						<tr>
							<td><label for="accField1">Extra Info</label></td>
							<td><input type="text" class="form-control" name=accField1 id="accField1" placeholder="Extra Info"></td>
						</tr>
						<tr>
							<td><label for="accNumber">Account Number</label></td>
							<td><input type="number" class="form-control" name="accNumber" id="accNumber" placeholder="Account Number"></td>
						</tr>
						<tr>
							<td colspan="2"><textarea rows="5" class="form-control" name="notes" id="notes"	placeholder="notes"></textarea></td>
						</tr>
						<tr>
							<td colspan="2"><button type="button" class="btn btn-success" onclick="doChanges()"style="float:right;">Update</button></td>
						</tr>    	
					</table>
				</div>
    		</div>     
 		</form>
	</div>
</div>

<!--  	<tr>
		<td>
		<div class="form-group form-check">
		    <input type="checkbox" class="form-check-input" id="disable">
		    <label class="form-check-label" for="disable">Do not use</label>
    		<input type="hidden" class="form-check-input" name="msg_updates" id="msg_updates">
    		<label class="form-check-label" for="msg_updates">Email order updates</label>
  		</div>
		</td>		
		<td>
			<div class="form-group form-check">		
				<input type="hidden" class="form-check-input" name="msg_mailshot" id="msg_mailshot">		
  				<label class="form-check-label" for="msg_mailshot">Email offers</label>  				
			</div>
		</td>		
	</tr>-->

