<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Employee form</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="js/validation.js"></script>

<style>
.mainclass
{
			width: 490px;
		    height: 250px;
		    margin: auto;
			background-color:cyan;
}
.error {
  color: red;
  margin-left: 5px;
}
.loginfields{
	padding: 8px 8px 8px 75px;
}
.input-data{
	padding: 3px 120px 3px 5px;
	margin-top:5px;
	margin-bottom:5px;
}
.h2-data{
	text-align: center;
	padding-top: 15px;
	margin-bottom: 15px;
}
.login-btn{
	padding: 5px 10px 5px 10px;
    background-color: #b9c7b9;
    border: none;
}
</style>
</head>
<body style="background-color: #c2fac2;margin-top:85px">
<div class="mainclass">
<h2 class="h2-data">Welcome To Invoice-Payslip-Tools</h2>
<form name="loginform" method="post" onsubmit="return validateform(this)"  action="save.html">
		<div class="loginfields">
			<div style="margin-bottom: 5px;">
			<span><b>Please Login :</b></span><br>
			</div>
			<div>
				<span><b>Enter Username :</b></span><br>
				<input type="text" id="uname" class="input-data">
				<span id="unameErr"></span>
			</div>
			<div>
				<span><b>Enter Password :</b></span><br>
				<input type="password" id="pswd" class="input-data">
				<span id="pwdErr"></span>
			</div>
		</div>		
		<center><input type="submit" value="Login" class="login-btn"/></center></form>
		</div>
</body>
</html>