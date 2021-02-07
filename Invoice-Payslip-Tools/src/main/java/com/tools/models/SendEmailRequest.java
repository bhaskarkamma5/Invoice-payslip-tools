package com.tools.models;

import javax.activation.DataSource;

public class SendEmailRequest {

	private String mailTo;

	private String mailCC;

	private String mailBCC;

	private String subject;

	private String content;
	
	private DataSource dataSource;
	
	private String fileName;
	
	private String CounterParty;
	
	private String finPeriod;
	
	private byte[] fileByteArray;
	
	private String folderName;
	
	private String CounterPartyId;

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailCC() {
		return mailCC;
	}

	public void setMailCC(String mailCC) {
		this.mailCC = mailCC;
	}

	public String getMailBCC() {
		return mailBCC;
	}

	public void setMailBCC(String mailBCC) {
		this.mailBCC = mailBCC;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCounterParty() {
		return CounterParty;
	}

	public void setCounterParty(String counterParty) {
		CounterParty = counterParty;
	}

	public String getFinPeriod() {
		return finPeriod;
	}

	public void setFinPeriod(String finPeriod) {
		this.finPeriod = finPeriod;
	}

	public byte[] getFileByteArray() {
		return fileByteArray;
	}

	public void setFileByteArray(byte[] fileByteArray) {
		this.fileByteArray = fileByteArray;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getCounterPartyId() {
		return CounterPartyId;
	}

	public void setCounterPartyId(String counterPartyId) {
		CounterPartyId = counterPartyId;
	}
	
}
