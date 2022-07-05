<html>
<head>
<link rel="shortcut icon" type="image/png"
	href="resources/images/favicon.png" />
<link href="resources/css/snackbar.css" rel="stylesheet">

<link href="resources/css/styles.css" rel="stylesheet" />

<link rel="stylesheet" href="resources/css/webtool.css" />

<script src="resources/js/jquery-3.6.0.min.js"></script>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>

<script src="resources/js/moment.js"></script>

<script src="resources/js/snackbar.js"></script>

<script src="https://cdn.jsdelivr.net/npm/js-cookie@rc/dist/js.cookie.min.js"></script>

 <script src="https://cdn.jsdelivr.net/npm/mermaid/dist/mermaid.min.js"></script>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/js/all.min.js"
	crossorigin="anonymous"></script>
 
</head>
<title><spring:eval expression="@environment.getProperty('website.title')" /></title>

<body onload="initscript()" class="container">

	<nav class="newNav">        	
		<div class="logo">
			<a class="navbar-brand" href="/"><img src="resources/images/favicon.png" alt="" class="logoimg">Org Charter</a>
		</div>
		<div class="navMenu flex-container">     	
			<div class="menuItem" onclick="action1()">action1</div>		
			<div class="menuItem" onclick="action2()">action2</div>			
			
			<div class="menuItem">
				<div class="dropdown-toggle" id="dropdownMore" data-bs-toggle="dropdown" aria-expanded="false">More</div>
					 <ul class="dropdown-menu dropdown-menu-end">
						 <sec:authorize access="isAuthenticated()">							
						 <sec:authorize access="hasAnyRole({'ROLE_ADMIN'})">
							 <li><a class="dropdown-item" href="/system/admin_main"><i class="fas fa-cog"></i> System</a></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole({'ROLE_ADMIN','ROLE_USER'})">
						<li><a class="dropdown-item" href="/logout2"><i class="fas fa-sign-out-alt"></i> logout</a></li>
						</sec:authorize>
						</sec:authorize>
						<li><a class="dropdown-item"><i class="far fa-question-circle"></i> Support</a></li>
						<li><a class="dropdown-item"><i class="fas fa-flask"></i> Request a feature</a></li>
					</ul>
				</div>
			
		</div>
	</nav>
          


<script type="text/javascript">	

 var csrfname="${_csrf.parameterName}";
 var csrfvalue="${_csrf.token}";  


 
	function initscript() {	
		 
		var config = { startOnLoad:true, 
			flowchart:{ useMaxWidth:false, htmlLabels:true, curve:'cardinal', },
			securityLevel:'loose',
			};		
		let insert = function (code) {
			$('#diag').html(code);
		  };
		const txt = "graph TD; A[Client] --> B[Load Balancer]; B --> C[Server01]; B --> D[Server02]";
		//pageStart(); // call others
		mermaid.initialize(config);
		
		$.getJSON('orgdata.json', function(dataIn){
			console.log(JSON.stringify(dataIn));
			mermaid.render("preparedScheme", dataIn.data, insert);
		}); 		
		
		// $('#diag').text("graph TD; A[Client] --> B[Load Balancer]; B --> C[Server01]; B --> D[Server02]");		

	}
		
</script>

    <div id="diag" class="mermaid flowchart">
    graph TD; A[loading...];
    </div>

<div id="msgdialog" class="modal fade" id="rowtitle" role="alert">
	<div class="modal-dialog" >
	  <div class="modal-content">        
		<div class="modal-header">              
		   <table width="100%"><tr ><td id="msgtitle"></tr></table>
		</div>
		
		<div class="modal-body">      
			<div id="msgdetails">		
		  </div>
	  </div>
	</div>
  </div>
  </div>
  
  <div id="snackbar">Some text some message..</div>


	
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
