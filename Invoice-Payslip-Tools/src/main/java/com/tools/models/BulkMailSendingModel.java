package com.tools.models;

public class BulkMailSendingModel {

	private String folderName;

	private String fileName = "Mail_Not-Sending(Pending)_Invoices";

	private SendEmailRequest mailRequest = new SendEmailRequest();

	private MeenakshiInvsExcelData invoiceData = new MeenakshiInvsExcelData();
	
	PlanBPayslipExcelData payslipData = new PlanBPayslipExcelData();

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public SendEmailRequest getMailRequest() {
		return mailRequest;
	}

	public void setMailRequest(SendEmailRequest mailRequest) {
		this.mailRequest = mailRequest;
	}

	public MeenakshiInvsExcelData getInvoiceData() {
		return invoiceData;
	}

	public void setInvoiceData(MeenakshiInvsExcelData invoiceData) {
		this.invoiceData = invoiceData;
	}

	public PlanBPayslipExcelData getPayslipData() {
		return payslipData;
	}

	public void setPayslipData(PlanBPayslipExcelData payslipData) {
		this.payslipData = payslipData;
	}

}
