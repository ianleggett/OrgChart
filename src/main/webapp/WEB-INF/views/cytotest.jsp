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

<script src="https://cdnjs.cloudflare.com/ajax/libs/cytoscape/3.22.1/cytoscape.min.js"></script>


    <script src="https://cdn.rawgit.com/maxkfranz/weaver/v1.2.0/dist/weaver.min.js"></script>
    <script src="https://cdn.rawgit.com/cytoscape/cytoscape.js-spread/1.3.1/cytoscape-spread.js"></script>

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
          

     <div id="cy"></div>

<script type="text/javascript">	

 var csrfname="${_csrf.parameterName}";
 var csrfvalue="${_csrf.token}";  
 
	function initscript() {	
		 		
		$.getJSON('orgdata.json', function(dataIn){
				console.log(JSON.stringify(dataIn));	
				
				window.cy = cytoscape({
					  container: document.getElementById('cy'),

					  boxSelectionEnabled: false,

					  style: [

						  {
							    "selector": ".bottom-center",
							    "style": {
							      "text-valign": "bottom",
							      "text-halign": "center"
							    }
							  },
						  {
							    "selector": ".outline",
							    "style": {
							    	"text-valign": "bottom",
								      "text-halign": "center",
							      "color": "#fff",
							      "text-outline-color": "#888",
							      "text-outline-width": 3
							    }
						  },
					    {
					      selector: 'node',
					      css: {
					        'content': 'data(label)',
					        'text-valign': 'bottom',
					        'text-halign': 'center',
					        "color": "#fff",
						      "text-outline-color": "#888",
						      "text-outline-width": 3
					      }
					    },
					    {
					      selector: ':parent',
					      css: {
					        'text-valign': 'top',
					        'text-halign': 'center',
					      }
					    },
					    {
					      selector: 'edge',
					      css: {
					        'curve-style': 'bezier',
					        'target-arrow-shape': 'triangle'
					      }
					    }
					  ],

					  elements: {"nodes":[{"data":{"id":"A1209","label":"Billy Blogs","parent":null}},
						  {"data":{"id":"A1067","text-wrap":"wrap","label":"Mike Magee<br/>Senior Engineering Manager","parent":null}},
						  {"data":{"id":"A1331","label":"Barry White<br/>Director of Engineering WMC","parent":null}},
						  {"data":{"id":"A1348","label":"Bill Bailey<br/>Senior Engineering Manager","parent":null}}],
						  "edges":[]},

					  layout: {
						  name: 'grid'
					       
					      },
					});
		}); 
		
		
	
		
		
		
		// $('#diag').text("graph TD; A[Client] --> B[Load Balancer]; B --> C[Server01]; B --> D[Server02]");		

	}
		
</script>


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
