<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Page</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="js/validation.js"></script>
<link rel=icon href=https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@5.14/svgs/solid/laptop-house.svg color=green>

<style>
.mainclass
{
			width: 490px;
		    height: 275px;
		    margin: auto;
			background-color:#87ffff;
}
.error {
  color: red;
  margin-left: 5px;
}
.loginfields{
	padding: 8px 8px 8px 75px;
}
.input-data{
	padding: 3px 5px 3px 5px;
	margin-top:5px;
	margin-bottom:5px;
	width:280px
}
.h2-data{
	text-align: center;
	padding-top: 15px;
	margin-bottom: 15px;
}
.login-btn{
	padding: 5px 10px 5px 10px;
    background-color: #fcba56;
    border: none;
}
h2 {
    color: #f35626;
    background-image: -webkit-linear-gradient(92deg, #f35626, #feab3a);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    -webkit-animation: hue 10s infinite linear;
}

@-webkit-keyframes hue {
    from {
      -webkit-filter: hue-rotate(0deg);
    }

    to {
      -webkit-filter: hue-rotate(360deg);
    }
}
</style>
</head>
<div id="responseMsg"></div>
<body style="background-color: #c4e8c4;margin-top:85px">
<div class="mainclass">
<h2 class="h2-data">Welcome To Invoice-Payslip-Tools</h2>
<form name="loginform" id="loginform">
		<div class="loginfields">
			<div style="margin-bottom: 5px;">
			<span><b>Please Login :</b></span><br>
			</div>
			<div>
				<span><b>Enter Username :</b></span><br>
				<input type="text" id="uname" class="input-data" name="uname">
				<br><span id="unameErr" class="error"></span>
			</div>
			<div>
				<span><b>Enter Password :</b></span><br>
				<input type="password" id="pswd" class="input-data" name="pswd">
				<br><span id="pwdErr" class="error"></span>
			</div>
		</div>		
		<center><input type="submit" value="Login" class="login-btn"/></center></form>
		</div>
</body>
</html>