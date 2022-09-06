<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<sec:authentication var="principal" property="principal" />

<script type="text/javascript">
	
	function initscript() {
		// populate views menu
		$.getJSON('viewdata.json', function(vdata) {
			$('#viewbase').empty();
			vdata.data.forEach(function(item){
				$('#viewbase').append('<li><a class="dropdown-item" href="diag?v='+item.name+'">'+item.name+'</a></li>');					
			});
			$('#viewbase').append('<li><a class="dropdown-item" href="viewEdit">edit views</a></li>');
		});	
		pageStart(); // call others
	}
		
	/* Hide the menu if user clicks outside the menu area */
	document.addEventListener("click", (evt) => {
    const flyoutElement = document.getElementById("mySidenav");
    const openMenu = document.getElementById("navOpen");
    let targetElement = evt.target; // clicked element

    do {
        if (targetElement == flyoutElement || targetElement == openMenu) {
            // This is a click inside. Do nothing, just return.
            return;
        }
        // Go up the DOM
        targetElement = targetElement.parentNode;
    } while (targetElement);   
});
	
	function viewButton(viewName){
		window.location.href="diag?v="+viewName
	}
</script>

<title><spring:eval
		expression="@environment.getProperty('website.title')" /></title>

<body onload="initscript()" class="zcontainer">

 <nav class="navbar navbar-expand-lg bg-light">
  <div class="container-fluid">
  <div class="logo">
	<a class="navbar-brand" href="/"><img src="/resources/images/favicon.png" alt="" class="logoimg">Org View</a>
  </div>
      
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
    
     <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">          
           <button type="button" onclick="viewButton('${view}')" class="btn btn-sm btn-info btn-rounded">${view}</button>
        </li>            
    </ul>
    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
     <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            Views
          </a>
          	<ul id="viewbase" class="dropdown-menu">
            <!--  Auto Filled -->
          </ul>
        </li>
    </ul>
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item"><a class="nav-link" href="staff">People</a>
					</li>
				</ul>

				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item"><a class="nav-link" href="containerEdit">Grouping</a>
					</li>
				</ul>

  <ul class="navbar-nav me-auto mb-2 mb-lg-0">
     <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            More
          </a>
          	<ul  class="dropdown-menu">
							<li><a class="dropdown-item" href="/uploadstaff"><i
									class="fas fa-file-import"></i>Import Staff Data</a></li>
							<li><a class="dropdown-item" href="/uploadgroup"><i
									class="fas fa-file-import"></i>Import Team Data</a></li>
							<li><a class="dropdown-item" href="/exportviewlist"><i
									class="fas fa-file-export"></i>Export View Data</a></li>
							<li><hr class="dropdown-divider"></li>
							<li><a class="dropdown-item disabled"><i
									class="far fa-question-circle"></i> Support</a></li>
							<li><a class="dropdown-item disabled"><i class="fas fa-flask"></i>
									Request a feature</a></li>
						</ul>
        </li>
    </ul>

    </div>
  </div>
</nav>
 
<main>           

	