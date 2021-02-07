<!DOCTYPE html>
<html lang="en">
<head>
    <title>Upload Excel File to MySQL</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/Invoice-Payslip-Tools/js/validation.js"></script>
</head>
 
<body style="background-color: #c4e8c4;">
	<div class="container h-100" style="background: #f1ce9f;margin-top: 8%;">	
	<div class="h-100">
		<div class="row h-100 justify-content-center align-items-center">		
			<div class="col-sm-5">		            
				<h3  style="margin-top: 12px;color: brown;">Upload Planb Payslips Excel File</h3>
				<form method="POST" enctype="multipart/form-data" id="fileUploadplanb" action="planbexcel">
					<div class="form-group">
						<label class="control-label" for="uploadfile">Upload File:</label>
						<a target="_self" style ="text-decoration: underline;
    							color: #00c585;" href="/Invoice-Payslip-Tools/dwnldtemplate/planb">Download Template File</a>
						<input type="file" class="form-control" id="uploadfile" placeholder="Upload File" 
						 name="uploadfile" style="padding: 0px; !important" onchange="return validatefile()"></input>
					</div>
					<button type="submit" class="btn btn-default" id="btnSubmit"
					style="margin-bottom: 8px;padding: 0px 10px 0px 10px;margin-top: -10px;margin-left: 160px;
					 background: #32f1b3;">Upload</button>
				</form>				
			</div>
		</div>
	</div>
	</div>	
	
</body>
</html>