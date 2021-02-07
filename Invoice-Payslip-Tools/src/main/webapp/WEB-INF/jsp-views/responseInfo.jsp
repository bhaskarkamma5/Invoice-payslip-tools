<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html style="background: whitesmoke;">
<head>
<style>
.msg-div{
	background: #f1ce9f;
    padding: 10px 0px 10px 0px;
    margin: auto;
    text-align: center;
    width: 490px;
}
</style>
</head>
	<body style="background: whitesmoke;">
		<div class="col-md-6 msg-div">
			<span style="color:#1fba1f"><b><%=request.getAttribute("msg")%></b></span>
		</div>		
	</body>
</html>