
$(document).ready(function () {

    $("#loginform").submit(function (event) {
        event.preventDefault();
        if(validateform()){
        	loginSubmit();
        }
    });
    $("#menakshi-org").click(function (event) {
        event.preventDefault();
        console.log("meenakshi-org")
        alert ( "Are you ready to upload Menakshi Invoices!");
        window.location.href = "/Invoice-Payslip-Tools/navigate/MenkshiInvUpload"
    });
    $("#planb-org").click(function (event) {
        event.preventDefault();
        console.log("planB-org")
        alert ( "Are you ready to upload Plan-B payslips!");
        window.location.href = "/Invoice-Payslip-Tools/navigate/PlanbexcelUpload"
    });
    $("#menakshi-einvoice-org").click(function (event) {
        event.preventDefault();
        console.log("meenakshi-einvoice-org")
        alert ( "Are you ready to send Menakshi E-invoices!");
        window.location.href = "/Invoice-Payslip-Tools/navigate/menakshiEinvoiceMail"
    });

});

function validateform(){  
	var uname=document.loginform.uname.value;  
	var pswd=document.loginform.pswd.value;  
	var pattern = /^([0-9]{4})-([0-9]{2})-([0-9]{2})$/;
	var unamePattern = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	document.getElementById("unameErr").innerHTML="";
	document.getElementById("pwdErr").innerHTML="";
	
	if(uname!=null && uname!="" && !unamePattern.test(uname)){	
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
	return true;
}  
function loginSubmit(){
	var search = {}
    search["username"] = $("#uname").val();
    search["pswd"] = $("#pswd").val();
    var url=location.pathname+"login";
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: url,
        data: JSON.stringify(search),
        success: function (data) {
            $('#responseMsg').html(data);
            console.log("SUCCESS : ", data);
            window.location.href = window.location+"navigate/org_home";
        },
        error: function (data) {
            if(data.status == 406)
            	$('#responseMsg').html(data.responseText);
            console.log("ERROR : ", data);
        }
    });
}	
function validatefile(){
	var fileInput = document.getElementById('uploadfile');       
    var filePath = fileInput.value; 
    var allowedExtensions = /(\.xlsx)$/i; 
	if (!allowedExtensions.exec(filePath)) { 
	    alert('Only Excel file with (.xlsx) extenstion is allowed'); 
	    fileInput.value = ''; 
	    return false; 
	}  
}