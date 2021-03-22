package com.tools.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.tools.models.BulkMailSendingModel;
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
				/* Font Styles End */

				Rectangle rect= new Rectangle(577,825,18,15); // you can resize rectangle 
			    rect.enableBorderSide(1);
			    rect.enableBorderSide(2);
			    rect.enableBorderSide(4);
			    rect.enableBorderSide(8);
			    rect.setBorderColor(BaseColor.BLACK);
			    rect.setBorderWidth(1);
			    document.add(rect);
			    
			    float tableWidth = 110;
			    
			    PdfPTable table = new PdfPTable(3);
				table.setWidthPercentage(tableWidth);
				PdfPCell cellOne = new PdfPCell();
				ClassPathResource resource = new ClassPathResource("/images/planb_logo_img.png");
				Image image1 = Image.getInstance(resource.getFile().getPath());
				image1.scaleAbsolute(150, 50);
				image1.setAlignment(Image.MIDDLE);
				cellOne = new PdfPCell(image1);
				cellOne.setHorizontalAlignment(Element.ALIGN_LEFT);
				cellOne.setPadding(5f);
				//cellOne.setPaddingBottom(5f);
				cellOne.setBorderWidthRight(0f);
				table.addCell(cellOne);
				String planbAddr = "\nPlan - B Software solutions Private Limited\n\n"+
				"Teacher's Colony, Vijayawada, Andhra Pradesh 520008";
				cellOne = new PdfPCell(new Phrase(planbAddr, textBOLD));
				cellOne.setHorizontalAlignment(Element.ALIGN_LEFT);
				cellOne.setBorderWidthLeft(0f);
				cellOne.setColspan(2);
				table.addCell(cellOne);
				table.setHorizontalAlignment(Element.ALIGN_CENTER);
				document.add(table);

				float[] columnWidths = { 45f, 70f, 45f, 70f};
				PdfPTable table1 = new PdfPTable(columnWidths);
				table1.setWidthPercentage(tableWidth);
				table1.setHorizontalAlignment(Element.ALIGN_CENTER);
				insertTableCell(table1, "Payslip for the month of " + payslip.getMonth(), Element.ALIGN_CENTER, 4, textBOLD,
						"ALL_NONE", "");
				
				/* New Code*/
				insertTableCell(table1, "Emp Name", Element.ALIGN_LEFT, 1, dataFont, "bottom-right", "");
				insertTableCell(table1, ": "+payslip.getEmpName(), Element.ALIGN_LEFT, 1, dataFont, "bottom-left-right", "");
				insertTableCell(table1, "Emp Code", Element.ALIGN_LEFT, 1, dataFont, "bottom-right", "");
				insertTableCell(table1, ": "+payslip.getEmpCode(), Element.ALIGN_LEFT, 1, dataFont, "bottom-left", "");
				insertTableCell(table1, "Pan Number", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table1, ": "+payslip.getPanNum(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA_RIGHT", "");
				insertTableCell(table1, "Join Date", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table1, ": "+payslip.getJoinDate(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA", "");
				insertTableCell(table1, "PF No", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table1, ": "+payslip.getPfNum(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA_RIGHT", "");
				insertTableCell(table1, "Department", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table1, ": "+payslip.getDepartment(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA", "");
				insertTableCell(table1, "ESIC", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table1, ": "+payslip.getEsiNum(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA_RIGHT", "");
				insertTableCell(table1, "Designation", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table1, ": "+payslip.getDesignation(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA", "");
				insertTableCell(table1, "Bank A/C No", Element.ALIGN_LEFT, 1, dataFont, "LAST_COL_1", "");
				insertTableCell(table1, ": "+payslip.getBankaccNum(), Element.ALIGN_LEFT, 1, dataFont, "LAST_COL_2L", "");
				insertTableCell(table1, "UAN Number", Element.ALIGN_LEFT, 1, dataFont, "LAST_COL_1", "");
				insertTableCell(table1, ": "+payslip.getUan(), Element.ALIGN_LEFT, 1, dataFont, "LAST_COL_2", "");
		
				insertTableCell(table1, "", Element.ALIGN_CENTER, 4, headerFont,"ALL_NONE", "");
				
				insertTableCell(table1, "Month Days", Element.ALIGN_LEFT, 1, dataFont, "bottom-right", "");
				insertTableCell(table1, ": "+payslip.getPresentDays()+"", Element.ALIGN_LEFT, 1, dataFont, "bottom-left-right", "");
				insertTableCell(table1, "LOP", Element.ALIGN_LEFT, 1, dataFont, "bottom-right", "");
				insertTableCell(table1, ": "+payslip.getLop()+"", Element.ALIGN_LEFT, 1, dataFont, "bottom-left", "");
				insertTableCell(table1, "Paid Leaves", Element.ALIGN_LEFT, 1, dataFont, "LAST_COL_1", "");
				insertTableCell(table1, ": "+payslip.getPaidLeaves()+"", Element.ALIGN_LEFT, 1, dataFont, "LAST_COL_2L", "");
				insertTableCell(table1, "Payable Days", Element.ALIGN_LEFT, 1, dataFont, "LAST_COL_1", "");
				insertTableCell(table1, ": "+payslip.getPayableDays()+"", Element.ALIGN_LEFT, 1, dataFont, "LAST_COL_2", "");
				
				document.add(table1);

				PdfPTable table2 = new PdfPTable(columnWidths);
				table2.setWidthPercentage(tableWidth);
				table2.setHorizontalAlignment(Element.ALIGN_CENTER);
				insertTableCell(table2, "", Element.ALIGN_CENTER, 4, headerFont,"ALL_NONE", "");
				insertTableCell(table2, "Earnings", Element.ALIGN_CENTER, 2, textBOLD, "right", "#A9A9A9");
				insertTableCell(table2, "Deductions", Element.ALIGN_CENTER, 2, textBOLD, "", " #A9A9A9");				
				
				insertTableCell(table2, "Basic+DA", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table2, ": "+payslip.getBasicAndDa(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA_RIGHT", "");
				insertTableCell(table2, "PF", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table2, ": "+payslip.getPf(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA", "");
				insertTableCell(table2, "HRA", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table2, ": "+payslip.getHra(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA_RIGHT", "");
				insertTableCell(table2, "ESI", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table2, ": "+payslip.getEsi(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA", "");
				insertTableCell(table2, "Conveyance", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table2, ": "+payslip.getConAllowed(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA_RIGHT", "");
				insertTableCell(table2, "PT", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table2, ": "+payslip.getProfTax(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA", "");
				insertTableCell(table2, "Basket Allowance", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table2, ": "+payslip.getBasketAllowence(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA_RIGHT", "");
				insertTableCell(table2, "IT", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table2, ": "+payslip.getItDeductions(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA", "");
				insertTableCell(table2, "Over Time", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table2, ": "+payslip.getBonus(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA_RIGHT", "");
				insertTableCell(table2, "", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table2, "", Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA", "");
				insertTableCell(table2, "Incentives", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table2, ": "+payslip.getBonus(), Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA_RIGHT", "");
				insertTableCell(table2, "", Element.ALIGN_LEFT, 1, dataFont, "DATA_HEADER", "");
				insertTableCell(table2, "", Element.ALIGN_LEFT, 1, dataFont, "DETAILS_DATA", "");
				
				insertTableCell(table2, "Total Earnings", Element.ALIGN_LEFT, 1, dataFont, "right", "");
				insertTableCell(table2, ": "+payslip.getTotalEarnings(), Element.ALIGN_LEFT, 1, dataFont, "right-left", "");
				insertTableCell(table2, "Total Deductions", Element.ALIGN_LEFT, 1, dataFont, "right", "");
				insertTableCell(table2, ": "+payslip.getTotalDeductions(), Element.ALIGN_LEFT, 1, dataFont, "left", "");
				
				insertTableCell(table2, "", Element.ALIGN_LEFT, 1, dataFont, "LAST_COL_1", "");
				insertTableCell(table2, "", Element.ALIGN_LEFT, 1, dataFont, "LAST_COL_2L", "");
				insertTableCell(table2, "Net Salary", Element.ALIGN_LEFT, 1, dataFont, "LAST_COL_1", "");
				insertTableCell(table2, ": "+payslip.getTotalPayable(), Element.ALIGN_LEFT, 1, dataFont, "LAST_COL_2", "");
				
				document.add(table2);				
				document.close();
				String date = new SimpleDateFormat("E_MMM_dd_HH_m_ss_z_yyyy").format(new Date());
				String fileName = payslip.getId() + "." + payslip.getEmpName() + "_" + payslip.getDesignation() + "_"
						+ payslip.getUnit();
				System.out.println(
						"------------> PDF Generation Completed for " + payslip.getEmpName() + " <-------------");
				multiPdfSource.add(new ByteArrayInputStream(out.toByteArray()));
				SendEmailRequest request = new SendEmailRequest();
				request.setMailTo(payslip.getMailTo());
				request.setMailCC(payslip.getMailCC());
				request.setMailBCC(payslip.getMailBCC());
				request.setSubject("Facilitation Invoice for " + payslip.getMonth());
				request.setFinPeriod(payslip.getMonth());
				request.setFileName(fileName);
				request.setFolderName(folder);
				request.setCounterParty(payslip.getId() + "_" + payslip.getEmpName());
				request.setCounterPartyId(payslip.getEmpCode());
				request.setFileByteArray(out.toByteArray());
				BulkMailSendingModel bulkMail = new BulkMailSendingModel();
				bulkMail.setFolderName(folder);
				bulkMail.setMailRequest(request);
				bulkMail.setPayslipData(payslip);
				planBMailSender.sendEmailWithAttachment(bulkMail);
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
		List<String> rightPostionList = Arrays.asList("DATA_HEADER","DETAILS_DATA_RIGHT","LAST_COL_1","LAST_COL_2L");
		List<String> leftPostionList = Arrays.asList("DETAILS_DATA","DETAILS_DATA_RIGHT","LAST_COL_2","LAST_COL_2L");
		List<String> bottomPostionList = Arrays.asList("DATA_HEADER","DETAILS_DATA","DETAILS_DATA_RIGHT","bottom");
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);		
		if (text.trim().equalsIgnoreCase("")) {
			cell.setMinimumHeight(15f);
		}
		if (!color.isEmpty() && color != null) {
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		}
		if (rightPostionList.contains(position)){
			cell.setBorderWidthRight(0);
			cell.setBorderWidthTop(0);
		}
		if (leftPostionList.contains(position)){
			cell.setBorderWidthLeft(0);
			cell.setBorderWidthTop(0);
		}
		if (position.equals("left")){
			cell.setBorderWidthLeft(0);
		}
		if (position.equals("right")){
			cell.setBorderWidthRight(0);
		}
		if (position.equals("right-left")){
			cell.setBorderWidthRight(0);
			cell.setBorderWidthLeft(0);
		}
		if (bottomPostionList.contains(position))
			cell.setBorderWidthBottom(0);
		if (position.equals("top-bottom")) {
			cell.setBorderWidthTop(0);
			cell.setBorderWidthBottom(0);
		}
		if (position.equals("bottom-right")) {
			cell.setBorderWidthRight(0);
			cell.setBorderWidthBottom(0);
		}
		if (position.equals("bottom-left")) {
			cell.setBorderWidthLeft(0);
			cell.setBorderWidthBottom(0);
		}
		if (position.equals("ALL_NONE")) {
			cell.setBorderWidthRight(0);
			cell.setBorderWidthBottom(0);
			cell.setBorderWidthTop(0);
			cell.setBorderWidthLeft(0);
		}
		if (position.equals("bottom-left-right")) {
			cell.setBorderWidthRight(0);
			cell.setBorderWidthBottom(0);
			cell.setBorderWidthLeft(0);
		}
		cell.setPadding(3f);
		List<String> heightPostionList = Arrays.asList("DATA_HEADER","DETAILS_DATA","DETAILS_DATA_RIGHT");
		/*if (!heightPostionList.contains(position)) {
			cell.setFixedHeight(45f);
			cell.setPadding(2f);
		}*/
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
			SendEmailRequest request = new SendEmailRequest();
			request.setMailTo("sskrajesh9@gmail.com,bhaskarkamma5@gmail.com");
			request.setSubject("PlanB PaySlip");
			request.setContent("Payslip Mails Processing Count '" + multiplePdf.size() + "'");
			request.setFileByteArray(out.toByteArray());
			request.setFileName("OverallInvoices");
			planBMailSender.sendRegularEmail(request);
			System.out.println("------------> OverallInvoices PDF Generation Completed <-------------");
		} catch (Exception e) {
			System.out.println("------------> OverallInvoices PDF Generation Issue <-------------");
			System.out.println("------------> OverallInvoices PDF Issue '" + e.getMessage() + "' <-------------");
		}
	}
}
