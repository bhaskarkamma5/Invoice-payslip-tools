<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Employee form</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="js/jquery11.js"></script>

<style>
div
{
			border:3px solid Blue;
			<!--text-align:center;-->
			width:50px;
			margin:5px;
			border-radius:20px;
			background-color:cyan;
}
.error {
  color: red;
  margin-left: 5px;
}
</style>
</head>
<body style="background-color:LightGreen;text-align:center">
<div style="width:100;hieght:100">
<h1>Employee adding Form</h1>
<form method="post" action="login" id="first_form">
		<center>
			<table style="with: 80%">
				<tr>
					<td>Employee Name</td>
					<td><input type="text" id="empname" name="empname" /></td>
				</tr>
				<tr>
					<td>EmployeeId</td>
					<td><input type="text" name="empid" id="empid"/></td>
				</tr>
				<tr>
					<td>Date of joining</td>
					<td><input type="text" id="doj" name="doj" /></td>
				</tr>
				<tr>
					<td>Salary</td>
					<td><input type="text" id="salary" name="salary" /></td>
				</tr>
				<tr><td>Dept name:</td><td>
					<select name="deptno" id="deptno" style="width:150px">
					<option value=0>---select----</option>
					</select></td>
				<td><a href="dept.jsp">add department</a>
				</tr>
				
				</table></center>
			<center><input type="submit" value="Submit" /></center></form>
			</div>
</body>
</html>