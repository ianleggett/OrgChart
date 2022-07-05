<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:include page="header.jsp" />

<style>
.wrapper {
	margin-top: 60px;
	margin-bottom: 20px;
}

.form-signin {
	max-width: 420px;
	padding: 30px 38px 66px;
	margin: 0 auto;
	background-color: #eee;
	border: 3px dotted rgba(0, 0, 0, 0.1);
}

.form-signin-heading {
	text-align: center;
	margin-bottom: 30px;
}

.form-control {
	position: relative;
	font-size: 16px;
	height: auto;
	padding: 10px;
}

input[type="text"] {
	margin-bottom: 0px;
	border-bottom-left-radius: 0;
	border-bottom-right-radius: 0;
}

input[type="password"] {
	margin-bottom: 20px;
	border-top-left-radius: 0;
	border-top-right-radius: 0;
}

.colorgraph {
	height: 7px;
	border-top: 0;
	background: #c4e17f;
	border-radius: 5px;
	background-image: -webkit-linear-gradient(left, #c4e17f, #c4e17f 12.5%, #f7fdca 12.5%,
		#f7fdca 25%, #fecf71 25%, #fecf71 37.5%, #f0776c 37.5%, #f0776c 50%,
		#db9dbe 50%, #db9dbe 62.5%, #c49cde 62.5%, #c49cde 75%, #669ae1 75%,
		#669ae1 87.5%, #62c2e4 87.5%, #62c2e4);
	background-image: -moz-linear-gradient(left, #c4e17f, #c4e17f 12.5%, #f7fdca 12.5%,
		#f7fdca 25%, #fecf71 25%, #fecf71 37.5%, #f0776c 37.5%, #f0776c 50%,
		#db9dbe 50%, #db9dbe 62.5%, #c49cde 62.5%, #c49cde 75%, #669ae1 75%,
		#669ae1 87.5%, #62c2e4 87.5%, #62c2e4);
	background-image: -o-linear-gradient(left, #c4e17f, #c4e17f 12.5%, #f7fdca 12.5%, #f7fdca
		25%, #fecf71 25%, #fecf71 37.5%, #f0776c 37.5%, #f0776c 50%, #db9dbe
		50%, #db9dbe 62.5%, #c49cde 62.5%, #c49cde 75%, #669ae1 75%, #669ae1
		87.5%, #62c2e4 87.5%, #62c2e4);
	background-image: linear-gradient(to right, #c4e17f, #c4e17f 12.5%, #f7fdca 12.5%, #f7fdca
		25%, #fecf71 25%, #fecf71 37.5%, #f0776c 37.5%, #f0776c 50%, #db9dbe
		50%, #db9dbe 62.5%, #c49cde 62.5%, #c49cde 75%, #669ae1 75%, #669ae1
		87.5%, #62c2e4 87.5%, #62c2e4);
}

.has-error {
	border-width: 3px;
	border-color: red;
}

.sampleDiv {
	width: 100%;
	text-align: center;	}
	
.sampleBut {
	width: 300px;
	margin-top: 30px;
	background-image: linear-gradient(to bottom right, #0b0987, #0b0987, silver);}
	
.sampleLink {
	color: #fff;}

.sampleLink:hover {
	color: #ffff66;
	text-decoration: none;
	font-weight: bold;}

.btn-primary {
	background-color: #0b0987;
	border-color: #0b0987;}

.btn-primary:hover {
	background-color: #0f0cc0;}
</style>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@page session="true"%>


<title>Login Page</title>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />


</head>
<body onload='document.f.username.focus();' style="background-color: #006EFF;">
       <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-5">
                                <div class="card shadow-lg border-0 rounded-lg mt-5">
                                    <div class="card-header" style="text-align: center;"><img src="/resources/images/tinance3-sm.png" style="width: 250px;" class="d-inline-block align-top" alt="">
                                    	V <spring:eval expression="@environment.getProperty('website.version')" />
                                    	<h3 class="text-center font-weight-light my-4">Login</h3>
                                    </div>
                                    <div class="card-body">
                                        <form method="POST" name='f' action="login">
                                            <div class="form-group ${error != null ? 'has-error' : ''}">
                                            <br /> <span>${message}</span>
                                                <label class="small mb-1" for="username">Username</label>
                                                <input name="username" id="username"type="text" class="form-control py-4" placeholder="Enter username" autofocus="autofocus" /> 
                                            </div>
                                            <div class="form-group">
                                                <label class="small mb-1" for="password">Password</label>
                                                <input name="password" type="password" id="password" class="form-control py-4" placeholder="Enter password" />
                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                                
                                            </div>
                                            <div style="text-align: center;">${error}</div>
                                            <div class="form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                                                <a class="small" href="password.html">Forgot Password?</a>
                                                <button class="btn btn-primary" type="submit">Login</button>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="card-footer text-center">
                                        <div class="small"><a href=#>Need an account? Sign up!</a></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
            <div id="layoutAuthentication_footer">
                <footer class="py-4 bg-light mt-auto">
                    <div class="container-fluid">
                        <div class="d-flex align-items-center justify-content-between small">
                            <div class="text-muted">Copyright &copy;Airex 2021</div>
                            <div>
                                <a href="#">Privacy Policy</a>
                                &middot;
                                <a href="#">Terms &amp; Conditions</a>
                            </div>
                        </div>
                    </div>
                </footer>
            </div>
        </div>


	<!-- ?${_csrf.parameterName}=${_csrf.token} -->
	<!-- input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/-->
	

</body>
</html>