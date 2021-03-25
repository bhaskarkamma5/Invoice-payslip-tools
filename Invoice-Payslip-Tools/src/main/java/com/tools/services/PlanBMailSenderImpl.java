package com.tools.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tools.models.BulkMailSendingModel;
import com.tools.models.PlanBPayslipExcelData;
import com.tools.models.ResponseModel;
import com.tools.models.SendEmailRequest;

@Service
public class PlanBMailSenderImpl implements PlanBMailSender {
	
	private final JavaMailSender javaMailSender = this.getJavaMailSender("sskrajesh9@gmail.com","zenmeenakshi");
	
	public JavaMailSender getJavaMailSender(final String userEmail, final String password) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername(userEmail);
		mailSender.setPassword(password);
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		//props.put("mail.debug", "true");
		return mailSender;
	}	
	
	public void sendEmail() {
		try {		
		MimeMessage msg = javaMailSender.createMimeMessage();
		MimeMessageHelper mail;		
		mail = new MimeMessageHelper(msg, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());		
		mail.setTo("bhaskarkamma5@gmail.com");
		mail.setSubject("Hi");
		mail.setText("welcome");
		javaMailSender.send(msg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	@Async
	public ResponseModel sendRegularEmail(SendEmailRequest sndEmailRequest) {
		ResponseModel response = new ResponseModel();
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper mail = new MimeMessageHelper(msg, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			if (sndEmailRequest.getMailTo() != null && !sndEmailRequest.getMailTo().isEmpty()) {
				mail.setTo(sndEmailRequest.getMailTo().split(","));
				if (sndEmailRequest.getMailCC() != null && !sndEmailRequest.getMailCC().isEmpty())
					mail.setCc(sndEmailRequest.getMailCC().split(","));
				if (sndEmailRequest.getMailBCC() != null && !sndEmailRequest.getMailBCC().isEmpty())
					mail.setBcc(sndEmailRequest.getMailBCC().split(","));
				mail.setSubject(sndEmailRequest.getSubject() != null ? sndEmailRequest.getSubject() : "");
				mail.setText(sndEmailRequest.getContent() != null ? sndEmailRequest.getContent() : "");
				if (sndEmailRequest.getFileByteArray() != null)
					mail.addAttachment(sndEmailRequest.getFileName()+".pdf",
							new ByteArrayDataSource(sndEmailRequest.getFileByteArray(), "application/pdf"));
				javaMailSender.send(msg);
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	@Async
	public ResponseModel sendEmailWithAttachment(BulkMailSendingModel bulkMailSender) {

		ResponseModel response = new ResponseModel();
		SendEmailRequest sndEmailRequest = bulkMailSender.getMailRequest();
		try {
			if (sndEmailRequest.getMailTo() != null && !sndEmailRequest.getMailTo().isEmpty()) {
				MimeMessage msg = javaMailSender.createMimeMessage();
				MimeMessageHelper mail = new MimeMessageHelper(msg, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
						StandardCharsets.UTF_8.name());
				mail.setTo(sndEmailRequest.getMailTo().split(","));
				if (sndEmailRequest.getMailCC() != null && !sndEmailRequest.getMailCC().isEmpty())
					mail.setCc(sndEmailRequest.getMailCC().split(","));
				if (sndEmailRequest.getMailBCC() != null && !sndEmailRequest.getMailBCC().isEmpty())
					mail.setBcc(sndEmailRequest.getMailBCC().split(","));
				mail.setSubject(sndEmailRequest.getSubject() != null ? sndEmailRequest.getSubject() : "");

				mail.setText("Please find the attached payslip for "
						+ sndEmailRequest.getFinPeriod(), true);

				String fileDestin = "F://" + sndEmailRequest.getFolderName() + "/" + sndEmailRequest.getFileName()
						+ ".pdf";
				mail.addAttachment(sndEmailRequest.getFileName()+".pdf",
						new ByteArrayDataSource(sndEmailRequest.getFileByteArray(), "application/pdf"));
				javaMailSender.send(msg);
				System.out.println("------------> MAIL SENT SUCCESSFULLY FOR CLIENT '"
						+ sndEmailRequest.getCounterParty() + "' <-------------");
				try {
					FileOutputStream fos = new FileOutputStream(new File(fileDestin));
					fos.write(sndEmailRequest.getFileByteArray());
					fos.flush();
					fos.close();
				} catch (Exception e) {
					try {
						FileOutputStream fos = new FileOutputStream(
								new File("F:\\" + sndEmailRequest.getFileName() + ".pdf"));
						fos.write(sndEmailRequest.getFileByteArray());
						fos.flush();
						fos.close();
					} catch (Exception fe) {
						System.out.println("------------> PDF NOT DOWNLOAD FOR CLIENT '"
								+ sndEmailRequest.getCounterParty() + "' <-------------");
						System.out.println("------------> PDF ISSUE '" + fe.getMessage() + "' <-------------");
					}
				}
				System.out.println("------------> PDF DOWNLOAD COMPLETED FOR CLIENT '"
						+ sndEmailRequest.getCounterParty() + "' <-------------");
				this.processingPyslipExcelForMissingMails(bulkMailSender, "MAIL_SUCCESS");
				Thread.sleep(5000);
			} else
				this.processingPyslipExcelForMissingMails(bulkMailSender, "MAIL_FAILED");
		} catch (Exception e) {
			System.out.println("------------> MAIL SENT ISSUE FOR CLIENT '" + sndEmailRequest.getCounterParty()
					+ "' <-------------");
			System.out.println("------------> MAIL SENT ISSUE  '" + e.getMessage() + "' <-------------");
			response.setErrorFound(true);
			this.processingPyslipExcelForMissingMails(bulkMailSender, "MAIL_FAILED"+e.getMessage());
		}
		return response;	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseModel processingPyslipExcelForMissingMails(BulkMailSendingModel bulkMailSender, String status) {
		ResponseModel response = new ResponseModel();
		PlanBPayslipExcelData payslipData = bulkMailSender.getPayslipData();
		try {
			String fileDestin = "F://" + bulkMailSender.getFolderName() + "/" + bulkMailSender.getFileName() + ".xlsx";
			File missingMailExcel = new File(fileDestin);
			XSSFWorkbook workBook = null;
			if (!missingMailExcel.exists()) {
				workBook = new XSSFWorkbook();
				XSSFSheet sheet = workBook.createSheet("Mail_issue_invoices");
				String headers[] = { "S.No.", "NAME_OF_THE_EMPLOYEE", "EMP_CODE", "Mail Status"};
				List<String> headerList = new ArrayList(Arrays.asList(headers));
				XSSFRow row = null;
				CellStyle headerStyle = workBook.createCellStyle();
				Font headerFont = workBook.createFont();
				headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
				headerFont.setFontHeightInPoints((short) 10);
				headerStyle.setFont(headerFont);
				row = sheet.createRow(0);
				int count = 0;
				for (String header : headerList) {
					Cell headercell = row.createCell(count);
					headercell.setCellValue(header);
					headercell.setCellStyle(headerStyle);
					sheet.autoSizeColumn(count);
					count++;
				}
				this.writeEinvoiceExcelData(workBook, sheet, payslipData, 1, status);
			} else {
				FileInputStream fis = new FileInputStream(missingMailExcel);
				workBook = new XSSFWorkbook(fis);
				XSSFSheet sheet = workBook.getSheetAt(0);
				this.writeEinvoiceExcelData(workBook, sheet, payslipData, sheet.getLastRowNum() + 1, status);
				fis.close();
			}
			ByteArrayOutputStream OutputStream = new ByteArrayOutputStream();
			workBook.write(OutputStream);
			FileOutputStream fos = new FileOutputStream(new File(fileDestin));
			fos.write(OutputStream.toByteArray());
			fos.flush();
			fos.close();
			OutputStream.close();
			System.out.println(
					"------------> " + payslipData.getEmpName() + " MAIL INFO Payslip FILE SAVED <-------------");
		} catch (Exception e) {
			System.out.println("------------> " + payslipData.getEmpName()
					+ " MAIL INFO FILE SAVING ISSUE <-------------");
			System.out.println("------------> EXCEL SAVING ISSUE " + e.getMessage() + " <-------------");
			response.setErrorFound(true);
		}
		return response;
	}
	
	public void writeEinvoiceExcelData(XSSFWorkbook workBook, XSSFSheet sheet, PlanBPayslipExcelData payslipData, int rowNum, String status) {

		CellStyle valuesStyle = workBook.createCellStyle();
		Font valueFont = workBook.createFont();
		valueFont.setFontHeightInPoints((short) 10);
		valuesStyle.setFont(valueFont);
		Cell cell = null;
		XSSFRow row = sheet.createRow(rowNum);
		cell = row.createCell(0);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(payslipData.getId());
		cell = row.createCell(1);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(payslipData.getEmpName());
		cell = row.createCell(2);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(payslipData.getEmpCode());
		cell = row.createCell(3);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(status);
	}
}
