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

        <nav class="newNav">        	
        	<div class="logo">
	            <a class="navbar-brand" href="/"><img src="/resources/images/favicon.png" alt="" class="logoimg">Org View</a>
			</div>			
            <div class="navMenu flex-container">
            <div class="menuItem"> <button type="button" onclick="viewButton('${view}')" class="btn btn-sm btn-info btn-rounded">${view}                        
                      </button>
                       </div>
            	<div class="menuItem">
            		<div class="dropdown-toggle" id="dropdownLang" data-bs-toggle="dropdown" aria-expanded="false">Views</div>
		             	<ul id="viewbase" class="dropdown-menu dropdown-menu-middle">						   
						   <!-- Auto Insert views here-->								    
						</ul>
            		</div>              		
            	<div class="menuItem"><a href="staff">People</a></div>          	
            	<div class="menuItem"><a href="containerEdit">Grouping</a></div>            	
            	<sec:authorize access="!isAuthenticated()">
            		<a class="menuItem" onclick="showlogin()" >Sign in</a>
            	</sec:authorize>
            	<sec:authorize access="isAuthenticated()">
            		<div id="btn-connect" class="menuItem menuGo">Connect</div>
				<sec:authorize access="hasAnyRole({'ROLE_ADMIN','ROLE_USER'})">
    				<div class="menuItem" onclick="myOffers()">Offers</div>
    				<div class="menuItem" onclick="showMyTrades()">Trades</div>
                </sec:authorize>
				</sec:authorize>            	
            	
            	<div class="menuItem">
            		<div class="dropdown-toggle" id="dropdownMore" data-bs-toggle="dropdown" aria-expanded="false">More</div>
		             	<ul class="dropdown-menu dropdown-menu-end">
			             	<sec:authorize access="isAuthenticated()">							<sec:authorize access="hasAnyRole({'ROLE_ADMIN'})">
	    				 		<li><a class="dropdown-item" href="/system/admin_main"><i class="fas fa-cog"></i> System</a></li>
	    				 		<div class="dropdown-item" onclick="deleteTrades()"><i class="fas fa-trash-alt"></i> trades</div>    				 		
	                		</sec:authorize>
	                		<sec:authorize access="hasAnyRole({'ROLE_ADMIN','ROLE_USER'})">
	                		<li><a class="dropdown-item" href="/logout2"><i class="fas fa-sign-out-alt"></i> logout</a></li>
							</sec:authorize>
							</sec:authorize>
							 <li><a class="dropdown-item" href="/uploadstaff"><i class="far fa-question-circle"></i>Import Staff Data</a></li>
							 <li><a class="dropdown-item" href="/uploadgroup"><i class="far fa-question-circle"></i>Import Team Data</a></li>
						    <li><a class="dropdown-item"><i class="far fa-question-circle"></i> Support</a></li>
						    <li><a class="dropdown-item"><i class="fas fa-flask"></i> Request a feature</a></li>
						    <li><a class="dropdown-item"><i class="fas fa-balance-scale"></i> Terms & Conditions</a></li>
						    <li><a class="dropdown-item"><i class="fas fa-user-secret"></i> Privacy Policy</a></li>
						</ul>
            		</div>            	
            </div>
		</nav>
           
            <!-- Navbar
            <div class="dropdown">
				<a class="btn dropdown-toggle text-light" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
					<i class="fas fa-user fa-fw"></i></a>
				<ul class="dropdown-menu dropdown-menu-end dropdown-menu-dark" aria-labelledby="dropdownMenuLink">
    				<li><a class="dropdown-item text-light" href="profile"><i class="fas fa-user"></i> Profile</a></li>
	    			<li><a class="dropdown-item text-light" href="#!"><i class="fas fa-cog"></i> Settings</a></li>
                    <sec:authorize access="isAuthenticated()">
					<sec:authorize access="hasAnyRole({'ROLE_ADMIN'})">
    				<li><a class="dropdown-item text-light" href="admin_main"><i class="fas fa-cog"></i> System</a></li>
                    </sec:authorize>
					</sec:authorize>
					<li><hr class="dropdown-divider"></li>
					<li><a class="dropdown-item text-light" href=logout><i class="fas fa-sign-out-alt"></i> Logout</a></li>
				</ul>
			</div>
        </nav>
        
        
        -->
        
<!--          <div id="layoutSidenav">
            <div id="layoutSidenav_nav">
                <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
                    <div class="sb-sidenav-menu">
                        <div class="nav">
                            <div class="sb-sidenav-menu-heading">Core</div>
                            <a class="nav-link" href="/">
                                <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                                Dashboard
                            </a>
                            <a class="nav-link" href="wallets">
                                <div class="sb-nav-link-icon"><i class="fas fa-poll"></i></div>
                                Wallets
                            </a>  
                             <a class="nav-link" href="browse">
                                <div class="sb-nav-link-icon"><i class="fas fa-poll"></i></div>
                                Browse
                            </a>                          
                            <a class="nav-link" href="offers">
                                <div class="sb-nav-link-icon"><i class="fas fa-poll"></i></div>
                                My Offers
                            </a>
                            <a class="nav-link" href="trades">
                                <div class="sb-nav-link-icon"><i class="fas fa-poll"></i></div>
                                My Trades
                            </a>                            
                            <a class="nav-link" href="ctr_pre">
                                <div class="sb-nav-link-icon"><i class="fas fa-poll"></i></div>
                                Ctr Test Page
                            </a>
                            <a class="nav-link" href="ctr_own">
                                <div class="sb-nav-link-icon"><i class="fas fa-poll"></i></div>
                                Ctr Owner Page
                            </a>
                            <div class="sb-sidenav-menu-heading">Settings</div>
                            
                            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseLayouts" aria-expanded="false" aria-controls="collapseLayouts">
                                <div class="sb-nav-link-icon"><i class="fas fa-columns"></i></div>
                                Settings
                                <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                            </a>                            
                            <div class="collapse" id="collapseLayouts" aria-labelledby="headingOne" data-parent="#sidenavAccordion">
                                <nav class="sb-sidenav-menu-nested nav">
                                                            
                                </nav>
                            </div>
                            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapsePages" aria-expanded="false" aria-controls="collapsePages">
                                <div class="sb-nav-link-icon"><i class="fas fa-book-open"></i></div>
                                Pages
                                <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                            </a>
                            <div class="collapse" id="collapsePages" aria-labelledby="headingTwo" data-parent="#sidenavAccordion">
                                <nav class="sb-sidenav-menu-nested nav accordion" id="sidenavAccordionPages">
                                    <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#pagesCollapseAuth" aria-expanded="false" aria-controls="pagesCollapseAuth">
                                        Authentication
                                        <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                                    </a>
                                    <div class="collapse" id="pagesCollapseAuth" aria-labelledby="headingOne" data-parent="#sidenavAccordionPages">
                                        <nav class="sb-sidenav-menu-nested nav">
                                            <a class="nav-link" href="login.html">Login</a>
                                            <a class="nav-link" href="register.html">Register</a>
                                            <a class="nav-link" href="password.html">Forgot Password</a>
                                        </nav>
                                    </div>
                                    <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#pagesCollapseError" aria-expanded="false" aria-controls="pagesCollapseError">
                                        Error
                                        <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                                    </a>
                                    <div class="collapse" id="pagesCollapseError" aria-labelledby="headingOne" data-parent="#sidenavAccordionPages">
                                        <nav class="sb-sidenav-menu-nested nav">
                                            <a class="nav-link" href="401.html">401 Page</a>
                                            <a class="nav-link" href="404.html">404 Page</a>
                                            <a class="nav-link" href="500.html">500 Page</a>
                                        </nav>
                                    </div>
                                </nav>
                            </div>
                            <div class="sb-sidenav-menu-heading">Addons</div>
                            <a class="nav-link" href="charts.html">
                                <div class="sb-nav-link-icon"><i class="fas fa-chart-area"></i></div>
                                Charts
                            </a>
                            <a class="nav-link" href="tables.html">
                                <div class="sb-nav-link-icon"><i class="fas fa-table"></i></div>
                                Tables
                            </a>
                        </div>
                    </div>
                    <div class="sb-sidenav-footer">
                        <div class="small">Logged in as:</div>
                        Start Bootstrap
                    </div>
                </nav>
            </div>
-->
<!-- <div id="layoutSidenav_content"> -->
  <main>           

	