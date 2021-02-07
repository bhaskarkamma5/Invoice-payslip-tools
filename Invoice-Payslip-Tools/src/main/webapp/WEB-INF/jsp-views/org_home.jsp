<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Page</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script type="text/javascript" src="/Invoice-Payslip-Tools/js/validation.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
.mainclass
{
			width: 700px;
		    height: 300px;
		    margin: auto;
			background-color:#87ffff;
}
.error {
  color: red;
  margin-left: 5px;
}
.h2-data{
	text-align: center;
	padding-top: 15px;
	margin-bottom: 15px;
	color: crimson;
}
.menakshi-org-icon{
	font-size: 85px;
    color: #ff5c00;
    margin-left: 100px;
}
.planb-org-icon{
	font-size: 65px;
    color: #2d3672;
    margin-top: 15px;
}
.plan-b{
	margin-left:0px !important;
	margin-right:100px;
}
span{
font-size:25px;
cursor: pointer;
}
.upload-icon{
	font-size: 15px;
    color: #2d3672;
    margin-top: 15px;
}
</style>
</head>
<div id="responseMsg"></div>
<body style="background-color: #c4e8c4;margin-top:85px">
<div class="mainclass">
<h2 class="h2-data">Choose organization</h2>
	<div class="row">
		<span id="menakshi-org">
		<i class="fa fa-home pull-left menakshi-org-icon">
		<br><span><i class="fa-upload upload-icon"></i> Menakshi</span>
		</i>
		</span>
		<span id="planb-org">
		<i class="fa fa-bold pull-right planb-org-icon plan-b"></i>
		<i class="fa fa-paypal pull-right planb-org-icon"><br>
		<span>PlanB</span>
		</i>
		</span>
	</div>
	<div class="row" style="clear:both;padding-top:17px">
		<span id="menakshi-einvoice-org">
		<i class="fa fa-home pull-left menakshi-org-icon">
		<br><span><i class="fa-upload upload-icon"></i> Menakshi E-invoices</span>
		</i>
		</span>
	</div>
</div>
</body>
</html>