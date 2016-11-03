<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html>
<body>
	<h1>
		Welcome, <small><sec:authentication property="principal" /></small> <small><sec:authentication property="userAuthentication" /></small>
	</h1>
</body>
</html>