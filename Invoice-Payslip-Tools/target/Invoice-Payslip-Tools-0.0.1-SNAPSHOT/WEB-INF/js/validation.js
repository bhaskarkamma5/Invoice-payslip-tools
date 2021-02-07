

function validateform(loginform){  
var uname=document.loginform.uname.value;  
var pswd=document.loginform.pswd.value;  
var pattern = /^([0-9]{4})-([0-9]{2})-([0-9]{2})$/;
var unamePattern = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
document.getElementById("unameErr").innerHTML="";
document.getElementById("pwdErr").innerHTML="";

if(uname!=null && !unamePattern.test(uname)){	
	document.getElementById("unameErr").innerHTML= "Please enter valid username";
}

if(uname==null || uname=="" || pswd==null || pswd==""){
	if (uname==null || uname==""){  
		  document.getElementById("unameErr").innerHTML="Please enter user name";
	}
	if(pswd==null || pswd==""){  
		document.getElementById("pwdErr").innerHTML="Please enter password";	
	}
	return false;
}

 
/*if(document.myform.deptno.selectedIndex=="")
{
alert ( "Please select department!");
return false;
}*/
}  
	