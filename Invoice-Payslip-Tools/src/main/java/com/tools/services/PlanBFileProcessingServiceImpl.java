package com.tools.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tools.models.PlanBPayslipExcelData;
import com.tools.models.ResponseModel;
import com.tools.models.SendEmailRequest;

@Service
public class PlanBFileProcessingServiceImpl implements PlanBFileProcessingService {
	
	@Autowired
	private PlanBMailSender planBMailSender;

	@SuppressWarnings("resource")
	@Override
	public ResponseModel ProcessPlanBExcelFile(MultipartFile file) {
		ResponseModel response = new ResponseModel();
		int currentRowNum = 0, currentColumnNum = 0;
		try {
			System.out.println("------------>PLAN B EXCEL File Reading Started <-------------");
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			int numberOfSheets = workbook.getNumberOfSheets();
			List<PlanBPayslipExcelData> payslipExcelList = new ArrayList<PlanBPayslipExcelData>();
			// looping over each workbook sheet
			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				Iterator<Row> rows = sheet.iterator();
				int rowNumber = 0;
				while (rows.hasNext()) {
					Row currentRow = rows.next();
					// skip header
					if (rowNumber == 0) {
						rowNumber++;
						continue;
					}
					Iterator<Cell> cellsInRow = currentRow.iterator();
					boolean isemptyCell = false;
					PlanBPayslipExcelData payslipData = new PlanBPayslipExcelData();
					currentRowNum = currentRow.getRowNum();
					while (cellsInRow.hasNext()) {
						Cell curCell = cellsInRow.next();
						isemptyCell = checkCellContainsDataOrNot(curCell);
						currentColumnNum = curCell.getColumnIndex();
						if (isemptyCell) {
							continue;
						}
						if (curCell.getColumnIndex() == 0)
							payslipData.setId(((Double) curCell.getNumericCellValue()) != null
									? ((Double) curCell.getNumericCellValue()).intValue() + "" : 0.0 + "");

						if (curCell.getColumnIndex() == 1)
							payslipData.setEmpName(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 2)
							payslipData.setEmpMailId(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 3)
							payslipData.setUan(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 4)
							payslipData.setPfNum(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 5)
							payslipData.setEsiNum(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 6)
							payslipData.setPanNum(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 7)
							payslipData.setUnit(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 8)
							payslipData.setBankaccNum(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 9)
							payslipData.setEmpCode(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");
						
						if (curCell.getColumnIndex() == 10)
							payslipData.setJoinDate(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");
						
						if (curCell.getColumnIndex() == 11)
							payslipData.setDepartment(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");
						
						if (curCell.getColumnIndex() == 12)
							payslipData.setDesignation(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 13)
							payslipData.setGross(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 14)
							payslipData.setPresentDays(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 15)
							payslipData.setPaidLeaves(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 16)
							payslipData.setLop(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 17)
							payslipData.setPayableDays(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 18)
							payslipData.setBasicAndDa(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 19)
							payslipData.setHra(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 20)
							payslipData.setConAllowed(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);
						
						if (curCell.getColumnIndex() == 21)
							payslipData.setBasketAllowence(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);
						
						if (curCell.getColumnIndex() == 22)
							payslipData.setBonus(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);
						
						if (curCell.getColumnIndex() == 23)
							payslipData.setTotalEarnings(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);
						
						if (curCell.getColumnIndex() == 24)
							payslipData.setProfTax(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);
						
						if (curCell.getColumnIndex() == 25)
							payslipData.setItDeductions(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);
						
						if (curCell.getColumnIndex() == 26)
							payslipData.setPf(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);
						
						if (curCell.getColumnIndex() == 27)
							payslipData.setEsi(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);
						
						if (curCell.getColumnIndex() == 28)
							payslipData.setOtherDeductions(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);
						
						if (curCell.getColumnIndex() == 29)
							payslipData.setTotalDeductions(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);
						
						if (curCell.getColumnIndex() == 30)
							payslipData.setTotalPayable(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 31)
							payslipData.setMonth(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

					}
					if (!payslipData.getEmpCode().isEmpty()) {
						payslipExcelList.add(payslipData);
						System.out.println("------------> EXCEL File Reading " + currentRow.getRowNum()
								+ " Row Completed <-------------");
					}
				}
			}
			System.out.println("------------>PLAN B EXCEL File Reading Completed Successfully <-------------");
			response = this.pdfGenerator(payslipExcelList);
		} catch (Exception e) {
			System.out.println("------------>PLAN B EXCEL File Reading Issue In " + currentRowNum + " Row Then Issue In "
					+ currentColumnNum + " Column <-------------");
			e.printStackTrace();
			response.setMessage("ERROR");
			response.setErrorFound(true);
		}
		return response;
	}

	private boolean checkCellContainsDataOrNot(Cell curCell) {

		boolean isContentNull = false;

		switch (curCell.getCellType()) {

		case Cell.CELL_TYPE_STRING:
			if (curCell.getStringCellValue() == null || curCell.getStringCellValue().isEmpty()
					|| "null".equalsIgnoreCase(curCell.getStringCellValue()))
				isContentNull = true;
			break;
		case Cell.CELL_TYPE_BLANK:
			isContentNull = true;
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if ((Double) curCell.getNumericCellValue() == null || (Double) curCell.getNumericCellValue() == 0)
				isContentNull = true;
			break;
		default:
			if (curCell.getDateCellValue() == null)
				isContentNull = true;
			break;
		}
		return isContentNull;
	}
	
	public ResponseModel pdfGenerator(List<PlanBPayslipExcelData> payslipData) {
		ResponseModel response = new ResponseModel();
		DecimalFormat df = new DecimalFormat("#.##");
		String folder = "planb_payslips" + new SimpleDateFormat("E_MMM_dd_HH_m_ss_z_yyyy").format(new Date());
		File file = new File("F:\\" + folder);
		file.mkdir();
		List<InputStream> multiPdfSource = new ArrayList<InputStream>();
		/* PDF START */
		payslipData.forEach(payslip -> {
			try {
				System.out.println(
						"------------> PDF Generation Started for " + payslip.getEmpName() + " <-------------");
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Document document = new Document(PageSize.A4, 60, 60, 120, 80);
				PdfWriter.getInstance(document, out);
				document.open();

				/* Font Styles Start */
				Font textBOLD = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
				Font dataFont = new Font(FontFamily.TIMES_ROMAN, 12);
				Font headerFont = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD);
				Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.GREEN);
				//PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
				/* Font Styles End */

				Rectangle rect= new Rectangle(577,825,18,15); // you can resize rectangle 
			    rect.enableBorderSide(1);
			    rect.enableBorderSide(2);
			    rect.enableBorderSide(4);
			    rect.enableBorderSide(8);
			    rect.setBorderColor(BaseColor.BLACK);
			    rect.setBorderWidth(1);
			    document.add(rect);
			     
				PdfPTable table = new PdfPTable(1);
				table.setWidthPercentage(100);
				PdfPCell cellOne = new PdfPCell();
				ClassPathResource resource = new ClassPathResource("/images/Meenakshi_Logo.bmp");
				Image image1 = Image.getInstance(resource.getFile().getPath());
				image1.scaleAbsolute(150, 50);
				image1.setAlignment(Image.MIDDLE);
				cellOne = new PdfPCell(image1);
				cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellOne.setPaddingTop(5f);
				cellOne.setBorderWidthRight(0f);
				//table.addCell(cellOne);
				String menkshiAddr = "Plan - B Software solutions Private Limited\n"
						+ "Teacher's Colony, Vijayawada, Andhra Pradesh 520008";
				cellOne = new PdfPCell(new Phrase(menkshiAddr, titleFont));
				cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
				//cellOne.setBorderWidthLeft(0f);
				table.addCell(cellOne);
				table.setWidthPercentage(100);
				table.setHorizontalAlignment(Element.ALIGN_CENTER);
				document.add(table);

				float[] columnWidths = { 28f, 50f, 28f, 50f};
				PdfPTable table1 = new PdfPTable(columnWidths);
				table1.setWidthPercentage(100);
				table1.setHorizontalAlignment(Element.ALIGN_CENTER);
				insertTableCell(table1, "Payslip for the month of " + payslip.getMonth(), Element.ALIGN_CENTER, 4, textBOLD,
						"ALL_NONE", "");
				String empDataHeader = "Emp Name\nPan Number\nPF No\nESIC\nBank A/C No";
				insertTableCell(table1, empDataHeader, Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				
				String empDetails = ": "+payslip.getEmpName() + "\n: " + payslip.getPanNum()
				+ "\n: " + payslip.getPfNum()+ "\n: " + payslip.getEsiNum()+ "\n: " + payslip.getBankaccNum();
				insertTableCell(table1, empDetails, Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA_RIGHT", "");
				
				String empDataHeader2 = "Emp Code\nJoin Date\nDepartment\nDesignation\nUAN Number";
				insertTableCell(table1, empDataHeader2, Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				
				String empData2 = ": " + payslip.getEmpCode() + "\n: " + payslip.getJoinDate()
				+ "\n: " + payslip.getDepartment() + "\n: " + payslip.getDesignation()+ "\n: " + payslip.getUan();
				insertTableCell(table1, empData2, Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA", "");
		
				insertTableCell(table1, "", Element.ALIGN_CENTER, 4, headerFont,"ALL_NONE", "");
				
				String empDataHeader3 = "Month Days\nPaid Leaves";
				insertTableCell(table1, empDataHeader3, Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				
				String empDetails3 = ": " + payslip.getPresentDays() + "\n: " + payslip.getPaidLeaves();
				insertTableCell(table1, empDetails3, Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA_RIGHT", "");
				
				String empDataHeader4 = "LOP\nPayable Days";
				insertTableCell(table1, empDataHeader4, Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				
				String empDetails4 = ": " + payslip.getLop() + "\n: " + payslip.getPayableDays();
				insertTableCell(table1, empDetails4, Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA", "");
				
				document.add(table1);

				PdfPTable table2 = new PdfPTable(columnWidths);
				table2.setWidthPercentage(100);
				table2.setHorizontalAlignment(Element.ALIGN_CENTER);
				insertTableCell(table2, "", Element.ALIGN_CENTER, 4, headerFont,"ALL_NONE", "");
				insertTableCell(table2, "Earnings", Element.ALIGN_CENTER, 2, textBOLD, "", "#A9A9A9");
				insertTableCell(table2, "Deductions", Element.ALIGN_CENTER, 2, textBOLD, "", " #A9A9A9");
				
				String salaryHeader = "Basic+DA\nHRA\nConveyance\nBasket Allowance\nOver Time\nincentives";
				insertTableCell(table2, salaryHeader, Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				
				String salaryDetails = ": " + payslip.getBasicAndDa() + "\n: " + payslip.getHra()
				+ "\n: " + payslip.getConAllowed()+ "\n: " + payslip.getBasketAllowence()
				+ "\n: " + 0.0+ "\n: " + payslip.getBonus();
				insertTableCell(table2, salaryDetails, Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA_RIGHT", "");
				
				String salaryHeader2 = "PF\nESI\nPT\nIT";
				insertTableCell(table2, salaryHeader2, Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				
				String salaryDetails2 = ": " + payslip.getPf() + "\n: " + payslip.getEsi()
				+ "\n: " + payslip.getProfTax() + "\n: " + payslip.getItDeductions();
				insertTableCell(table2, salaryDetails2, Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA", "");
				
				insertTableCell(table2, "Total Earnings : "+payslip.getTotalEarnings(), Element.ALIGN_CENTER, 2, textBOLD, "", "");
				insertTableCell(table2, "Total Deductions : "+payslip.getTotalDeductions(), Element.ALIGN_CENTER, 2, textBOLD, "", "");
				
				insertTableCell(table2, "Net Salary : "+payslip.getTotalPayable(), Element.ALIGN_RIGHT, 4, textBOLD, "head", "");
				document.add(table2);				
				document.close();
				String date = new SimpleDateFormat("E_MMM_dd_HH_m_ss_z_yyyy").format(new Date());
				String fileName = payslip.getId() + "." + payslip.getEmpName() + "_" + payslip.getDesignation() + "_"
						+ payslip.getUnit();
				System.out.println(
						"------------> PDF Generation Completed for " + payslip.getEmpName() + " <-------------");
				multiPdfSource.add(new ByteArrayInputStream(out.toByteArray()));
				/*SendEmailRequest request = new SendEmailRequest();
				request.setMailTo(invoice.getMailTo());
				request.setMailCC(invoice.getMailCC());
				request.setMailBCC(invoice.getMailBCC());
				request.setSubject("Facilitation Invoice for " + invoice.getMonth());
				request.setFinPeriod(invoice.getMonth());
				request.setFileName(fileName);
				request.setFolderName(folder);
				request.setCounterParty(invoice.getId() + "_" + invoice.getOwnerName());
				request.setCounterPartyId(invoice.getInvNum());
				request.setFileByteArray(out.toByteArray());
				BulkMailSendingModel bulkMail = new BulkMailSendingModel();
				bulkMail.setFolderName(folder);
				bulkMail.setMailRequest(request);
				bulkMail.setInvoiceData(invoice);*/
				//mailService.sendEmailWithAttachment(bulkMail);
			} catch (Exception e) {
				System.out.println("------------> PDF Generation Issue With Client  " + payslip.getEmpName()
						+ " <-------------");
				e.printStackTrace();
				response.setErrorFound(true);
			}
		});
		this.allPdfDocs(multiPdfSource, folder);
		return response;
	}

	private void insertTableCell(PdfPTable table, String text, int align, int colspan, Font font, String position,
			String color) {
		PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
		if (text.trim().equalsIgnoreCase("")) {
			cell.setMinimumHeight(15f);
		}
		if (!color.isEmpty() && color != null) {
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		}
		if (position.equalsIgnoreCase("DATA_HEADER") || position.equalsIgnoreCase("DETAILS_DATA_RIGHT"))
			cell.setBorderWidthRight(0);
		if (position.equalsIgnoreCase("DETAILS_DATA") || position.equalsIgnoreCase("DETAILS_DATA_RIGHT"))
			cell.setBorderWidthLeft(0);
		if (position.equals("bottom"))
			cell.setBorderWidthBottom(0);
		if (position.equals("top-bottom")) {
			cell.setBorderWidthTop(0);
			cell.setBorderWidthBottom(0);
		}
		if (position.equals("bottom-right")) {
			cell.setBorderWidthRight(0);
			cell.setBorderWidthBottom(0);
		}
		if (position.equals("ALL_NONE")) {
			cell.setBorderWidthRight(0);
			cell.setBorderWidthBottom(0);
			cell.setBorderWidthTop(0);
			cell.setBorderWidthLeft(0);
		}
		cell.setFixedHeight(45f);
		cell.setPaddingBottom(2f);
		table.addCell(cell);
	}
	
	@SuppressWarnings("deprecation")
	public void allPdfDocs(List<InputStream> multiplePdf, String folderName) {
		try {
			System.out.println("------------> OverallInvoices PDF Generation Start <-------------");
			PDFMergerUtility ut = new PDFMergerUtility();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ut.addSources(multiplePdf);
			String fileDestin = "F://" + folderName + "/" + "OverallPaySlips" + ".pdf";
			ut.setDestinationFileName(fileDestin);
			ut.setDestinationStream(out);
			ut.mergeDocuments();
			try {
				FileOutputStream fos = new FileOutputStream(new File(fileDestin));
				fos.write(out.toByteArray());
				fos.flush();
				fos.close();
			} catch (Exception e) {
				FileOutputStream fos = new FileOutputStream(new File("F:\\OverallPaySlips.pdf"));
				fos.write(out.toByteArray());
				fos.flush();
				fos.close();
			}
			/*SendEmailRequest request = new SendEmailRequest();
			request.setMailTo("sskrajesh9@gmail.com,bhaskarkamma5@gmail.com");
			request.setSubject("PlanB PaySlip");
			request.setContent("Payslip Mails Processing Count '" + multiplePdf.size() + "'");
			request.setFileByteArray(out.toByteArray());
			request.setFileName("OverallInvoices");
			planBMailSender.sendRegularEmail(request);*/
			System.out.println("------------> OverallInvoices PDF Generation Completed <-------------");
		} catch (Exception e) {
			System.out.println("------------> OverallInvoices PDF Generation Issue <-------------");
			System.out.println("------------> OverallInvoices PDF Issue '" + e.getMessage() + "' <-------------");
		}
	}
}
