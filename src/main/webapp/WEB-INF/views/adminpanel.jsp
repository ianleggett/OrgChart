<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<sec:authentication var="principal" property="principal" />

<html>
<head>
<link rel="shortcut icon" type="image/png" href="/resources/images/favicon.png" />
<link href="/resources/css/snackbar.css" rel="stylesheet">

<link href="/resources/css/styles.css" rel="stylesheet" />

<link rel="stylesheet" href="/resources/css/webtool.css" />

<script src="/resources/js/jquery-3.6.0.min.js"></script>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>

<script src="/resources/js/moment.js"></script>

<script src="/resources/js/snackbar.js"></script>

<script src="https://cdn.jsdelivr.net/npm/js-cookie@rc/dist/js.cookie.min.js"></script>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/js/all.min.js"
	crossorigin="anonymous"></script>
 
<script type="text/javascript">
	
	function initscript() {
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
</script>
</head>
<title><spring:eval
		expression="@environment.getProperty('website.title')" /></title>

<body onload="initscript()" class="container">
	<nav class="newNav">        	
		<div class="logo">
			<a class="navbar-brand" href="/"><img src="/resources/images/favicon.png" alt="" class="logoimg">Trade Finance</a>
		</div>
		<div class="navMenu flex-container">
		 <sec:authorize access="isAuthenticated()">
			<sec:authorize access="hasAnyRole({'ROLE_ADMIN'})">		
				<div class="menuItem"><a class="dropdown-item" href="/system/admin_main">settings</a></div>
				<div class="menuItem"><a class="dropdown-item" href="/system/fact_emails">emails</a></div>
				<div class="menuItem"><a class="dropdown-item" href="/system/ctr_pre">ViewCTR</a></div>
				<div class="menuItem"><a class="dropdown-item" href="/system/arbconsole">arbitration</a></div>
				<div class="menuItem"><a class="dropdown-item" href="/system/logout2">logout</a></div>					
				<div class="menuItem"><a class="dropdown-item" href="/system/resetemaildata">reset-email-data</a></div>
			</sec:authorize>
			</sec:authorize>            	
			

		</div>
	</nav>
          

</body>