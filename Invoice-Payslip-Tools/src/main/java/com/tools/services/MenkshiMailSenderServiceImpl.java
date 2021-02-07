package com.tools.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tools.models.BulkMailSendingModel;
import com.tools.models.MeenakshiInvsExcelData;
import com.tools.models.ResponseModel;
import com.tools.models.SendEmailRequest;

@Service
public class MenkshiMailSenderServiceImpl implements MenkshiMailSenderService {

	@Autowired
	private JavaMailSender javaMailSender;

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

				mail.setText("Dear Sir/Madam," + "<br>" + "          Please find the attached Facilitation invoice for "
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
				Thread.sleep(5000);
			} else
				this.processingExcelForMissingMails(bulkMailSender.getInvoiceData(), bulkMailSender.getFolderName(),
						bulkMailSender.getFileName());
		} catch (Exception e) {
			System.out.println("------------> MAIL SENT ISSUE FOR CLIENT '" + sndEmailRequest.getCounterParty()
					+ "' <-------------");
			System.out.println("------------> MAIL SENT ISSUE  '" + e.getMessage() + "' <-------------");
			response.setErrorFound(true);
			this.processingExcelForMissingMails(bulkMailSender.getInvoiceData(), bulkMailSender.getFolderName(),
					bulkMailSender.getFileName());
		}
		return response;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseModel processingExcelForMissingMails(MeenakshiInvsExcelData invData, String folderName, String fileName) {
		ResponseModel response = new ResponseModel();
		try {
			String fileDestin = "F://" + folderName + "/" + fileName + ".xlsx";
			File missingMailExcel = new File(fileDestin);
			XSSFWorkbook workBook = null;
			if (!missingMailExcel.exists()) {
				workBook = new XSSFWorkbook();
				XSSFSheet sheet = workBook.createSheet("Mail_issue_invoices");
				String headers[] = { "S.No.", "Site", "Type", "Month", "Date", "Inv No", "Name of the Owner", "Address",
						"PAN Number", "GST Number", "AX Vendor code", "AX Customer code", "QTY", "Rate",
						"Taxable Value", "CGST@9%", "SGST@9%", "IGST@18%", "Total Invoice Value", "Mail Id to",
						"Mail Id CC", "Mail Id BCC" };
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
				this.writeExcelData(workBook, sheet, invData, 1);
			} else {
				FileInputStream fis = new FileInputStream(missingMailExcel);
				workBook = new XSSFWorkbook(fis);
				XSSFSheet sheet = workBook.getSheetAt(0);
				this.writeExcelData(workBook, sheet, invData, sheet.getLastRowNum() + 1);
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
					"------------> " + invData.getOwnerName() + " MAIL NOT SENDING INVOICE FILE SAVED <-------------");
		} catch (Exception e) {
			System.out.println("------------> " + invData.getOwnerName()
					+ " MAIL NOT SENDING INVOICES FILE SAVING ISSUE <-------------");
			System.out.println("------------> EXCEL SAVING ISSUE " + e.getMessage() + " <-------------");
			response.setErrorFound(true);
		}
		return response;
	}

	public void writeExcelData(XSSFWorkbook workBook, XSSFSheet sheet, MeenakshiInvsExcelData invData, int rowNum) {

		CellStyle valuesStyle = workBook.createCellStyle();
		Font valueFont = workBook.createFont();
		valueFont.setFontHeightInPoints((short) 10);
		valuesStyle.setFont(valueFont);
		Cell cell = null;
		XSSFRow row = sheet.createRow(rowNum);
		cell = row.createCell(0);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getId());
		cell = row.createCell(1);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getSite());
		cell = row.createCell(2);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getType());
		cell = row.createCell(3);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getMonth());
		cell = row.createCell(4);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getInvDate());
		cell = row.createCell(5);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getInvNum());
		cell = row.createCell(6);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getOwnerName());
		cell = row.createCell(7);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getAddress());
		cell = row.createCell(8);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getPanNum());
		cell = row.createCell(9);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getGstin());
		cell = row.createCell(10);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getVendCodeAX());
		cell = row.createCell(11);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getCutomrCodeAX());
		cell = row.createCell(12);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getQty());
		cell = row.createCell(13);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getRate());
		cell = row.createCell(14);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getTxVal());
		cell = row.createCell(15);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getCgst());
		cell = row.createCell(16);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getSgst());
		cell = row.createCell(17);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getIgst());
		cell = row.createCell(18);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getTotalVal());
		cell = row.createCell(19);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getMailTo());
		cell = row.createCell(20);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getMailCC());
		cell = row.createCell(21);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getMailBCC());
	}

	@Override
	@Async
	public ResponseModel sendEinvoiceEmailWithAttachment(BulkMailSendingModel bulkMailSender) {
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

				mail.setText("Dear Sir/Madam," + "<br>" + "          Please find the attached Facilitation E-invoice for "
						+ sndEmailRequest.getFinPeriod(), true);
				
				mail.addAttachment(sndEmailRequest.getFileName()+".pdf",
						new ByteArrayDataSource(sndEmailRequest.getFileByteArray(), "application/pdf"));
				javaMailSender.send(msg);
				System.out.println("------------> MAIL SENT SUCCESSFULLY FOR CLIENT '"
						+ sndEmailRequest.getCounterParty() + "' <-------------");
				this.processingEnvoiceExcelForMissingMails(bulkMailSender, "MAIL_SUCCESS");
				Thread.sleep(5000);
			} else
				this.processingEnvoiceExcelForMissingMails(bulkMailSender, "MAIL_FAILED");
		} catch (Exception e) {
			System.out.println("------------> MAIL SENT ISSUE FOR CLIENT '" + sndEmailRequest.getCounterParty()
					+ "' <-------------");
			System.out.println("------------> MAIL SENT ISSUE  '" + e.getMessage() + "' <-------------");
			response.setErrorFound(true);
			this.processingEnvoiceExcelForMissingMails(bulkMailSender, "MAIL_FAILED"+e.getMessage());
		}
		return response;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseModel processingEnvoiceExcelForMissingMails(BulkMailSendingModel bulkMailSender, String status) {
		ResponseModel response = new ResponseModel();
		MeenakshiInvsExcelData invData = bulkMailSender.getInvoiceData();
		try {
			String fileDestin = "F://" + bulkMailSender.getFolderName() + "/" + bulkMailSender.getFileName() + ".xlsx";
			File missingMailExcel = new File(fileDestin);
			XSSFWorkbook workBook = null;
			if (!missingMailExcel.exists()) {
				workBook = new XSSFWorkbook();
				XSSFSheet sheet = workBook.createSheet("Mail_issue_invoices");
				String headers[] = { "S.No.", "Site", "Type", "Month", "Inv No", "Name of the Owner", "Mail Id to", "Mail Status"};
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
				this.writeEinvoiceExcelData(workBook, sheet, invData, 1, status);
			} else {
				FileInputStream fis = new FileInputStream(missingMailExcel);
				workBook = new XSSFWorkbook(fis);
				XSSFSheet sheet = workBook.getSheetAt(0);
				this.writeEinvoiceExcelData(workBook, sheet, invData, sheet.getLastRowNum() + 1, status);
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
					"------------> " + invData.getOwnerName() + " MAIL INFO INVOICE FILE SAVED <-------------");
		} catch (Exception e) {
			System.out.println("------------> " + invData.getOwnerName()
					+ " MAIL INFO FILE SAVING ISSUE <-------------");
			System.out.println("------------> EXCEL SAVING ISSUE " + e.getMessage() + " <-------------");
			response.setErrorFound(true);
		}
		return response;
	}
	
	public void writeEinvoiceExcelData(XSSFWorkbook workBook, XSSFSheet sheet, MeenakshiInvsExcelData invData, int rowNum, String status) {

		CellStyle valuesStyle = workBook.createCellStyle();
		Font valueFont = workBook.createFont();
		valueFont.setFontHeightInPoints((short) 10);
		valuesStyle.setFont(valueFont);
		Cell cell = null;
		XSSFRow row = sheet.createRow(rowNum);
		cell = row.createCell(0);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getId());
		cell = row.createCell(1);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getSite());
		cell = row.createCell(2);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getType());
		cell = row.createCell(3);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getMonth());
		cell = row.createCell(4);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getInvNum());
		cell = row.createCell(5);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getOwnerName());
		cell = row.createCell(6);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(invData.getMailTo());
		cell = row.createCell(7);
		cell.setCellStyle(valuesStyle);
		cell.setCellValue(status);
	}
}
