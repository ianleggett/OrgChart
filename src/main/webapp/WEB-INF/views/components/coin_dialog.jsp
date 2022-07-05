
<div id="coinDialog" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" >  
    <!-- Modal content-->
    <div class="modal-content">
       <div class="modal-header"> <h2>Add/Edit Coin</h2>     
       <button type="button" class="close" data-dismiss="modal">&times;</button>
       </div>
       <div class="modal-body">      		
       <form id="coin_form">      
       <table class="table-striped table-bordered" width="100%">
       <tr>		
			<td align="right"><div class="col">Coin Name<i style="color:red;">*</i></div></td>			
			<td><div class="col">	
			   <select class="form-control" id="coinid"></select>			 
				</div> 
			</td></tr>			
		    <tr>				
			<tr>
			<td align="right"><div class="col">Token Address<i style="color:red;">*</i></div></td>			
			<td><div class="col">	
					  <input type="text" class="form-control" id="walletAddr"></input>					 
				</div> 
			</td>			
			<tr>
			<td>					
       		 </td><td align="right">			
					<div class="col">
       		 			<button onclick="addUpdateCoin()" type="button" class="btn btn-success">OK</button>
       		 		</div>
       		 </td></tr>      		
       		</table>	
       		</form> 
	   </div>      
      </div>     
    </div>
</div>