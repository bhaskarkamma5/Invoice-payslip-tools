package com.tools.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jdt.internal.compiler.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tools.models.BulkMailSendingModel;
import com.tools.models.MeenakshiInvsExcelData;
import com.tools.models.ResponseModel;
import com.tools.models.SendEmailRequest;

@Service
public class MenkshiFileProcessingServiceImpl implements MenkshiFileProcessingService {

	@Autowired
	private MenkshiMailSenderService mailService;

	@SuppressWarnings("resource")
	@Override
	public ResponseModel ProcessExcelFile(MultipartFile file) {
		ResponseModel response = new ResponseModel();
		int currentRowNum = 0, currentColumnNum = 0;
		try {
			System.out.println("------------> EXCEL File Reading Started <-------------");
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			int numberOfSheets = workbook.getNumberOfSheets();
			List<MeenakshiInvsExcelData> invioceExcelList = new ArrayList<MeenakshiInvsExcelData>();
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
					MeenakshiInvsExcelData InvioceData = new MeenakshiInvsExcelData();
					currentRowNum = currentRow.getRowNum();
					while (cellsInRow.hasNext()) {
						Cell curCell = cellsInRow.next();
						isemptyCell = checkCellContainsDataOrNot(curCell);
						currentColumnNum = curCell.getColumnIndex();
						if (isemptyCell) {
							continue;
						}
						if (curCell.getColumnIndex() == 0)
							InvioceData.setId(((Double) curCell.getNumericCellValue()) != null
									? ((Double) curCell.getNumericCellValue()).intValue() + "" : 0.0 + "");

						if (curCell.getColumnIndex() == 1)
							InvioceData.setSite(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 2)
							InvioceData.setType(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 3)
							InvioceData.setMonth(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 4)
							InvioceData.setInvDate(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 5)
							InvioceData.setInvNum(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 6)
							InvioceData.setOwnerName(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 7)
							InvioceData.setAddress(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 8)
							InvioceData.setPanNum(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 9)
							InvioceData.setGstin(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 10)
							InvioceData.setVendCodeAX(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 11)
							InvioceData.setCutomrCodeAX(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 12)
							InvioceData.setQty(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 13)
							InvioceData.setRate(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 14)
							InvioceData.setTxVal(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 15)
							InvioceData.setCgst(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 16)
							InvioceData.setSgst(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 17)
							InvioceData.setIgst(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 18)
							InvioceData.setTotalVal(((Double) curCell.getNumericCellValue()) != null
									? curCell.getNumericCellValue() : 0.0);

						if (curCell.getColumnIndex() == 19)
							InvioceData.setMailTo(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 20)
							InvioceData.setMailCC(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 21)
							InvioceData.setMailBCC(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

					}
					if (!InvioceData.getInvNum().isEmpty()) {
						invioceExcelList.add(InvioceData);
						System.out.println("------------> EXCEL File Reading " + currentRow.getRowNum()
								+ " Row Completed <-------------");
					}
				}
			}
			System.out.println("------------> EXCEL File Reading Completed Successfully <-------------");
			response = this.pdfGenerator(invioceExcelList);
		} catch (Exception e) {
			System.out.println("------------> EXCEL File Reading Issue In " + currentRowNum + " Row Then Issue In "
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

	public ResponseModel pdfGenerator(List<MeenakshiInvsExcelData> invoicesData) {
		ResponseModel response = new ResponseModel();
		DecimalFormat df = new DecimalFormat("#.##");
		String folder = "Meenakshi_Invoices" + new SimpleDateFormat("E_MMM_dd_HH_m_ss_z_yyyy").format(new Date());
		File file = new File("F:\\" + folder);
		file.mkdir();
		List<InputStream> multiPdfSource = new ArrayList<InputStream>();
		/* PDF START */
		invoicesData.forEach(invoice -> {
			try {
				System.out.println(
						"------------> PDF Generation Started for " + invoice.getOwnerName() + " <-------------");
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Document document = new Document(PageSize.A4);
				PdfWriter.getInstance(document, out);
				document.open();

				/* Font Styles Start */
				Font textBOLD = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
				Font dataFont = new Font(FontFamily.TIMES_ROMAN, 12);
				Font headerFont = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD);
				/* Font Styles End */

				PdfPTable table = new PdfPTable(2);
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
				table.addCell(cellOne);
				String menkshiAddr = "Meenakshi Infrastructures Pvt.Ltd\n119, Road #10, Jubilee Hills\n"
						+ "Hyderabad - 500 033, India\nTel: 040 67331234\nGSTIN: 36AAECM0206D1Z5";
				cellOne = new PdfPCell(new Phrase(menkshiAddr, textBOLD));
				cellOne.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cellOne.setBorderWidthLeft(0f);
				table.addCell(cellOne);
				table.setWidthPercentage(100);
				table.setHorizontalAlignment(Element.ALIGN_CENTER);
				document.add(table);

				PdfPTable table1 = new PdfPTable(2);
				table1.setWidthPercentage(100);
				table1.setHorizontalAlignment(Element.ALIGN_CENTER);
				insertTableCell(table1, "Tax Invoice for " + invoice.getMonth(), Element.ALIGN_CENTER, 2, headerFont,
						"head", " #A9A9A9");
				insertTableCell(table1, "Bill to Party", Element.ALIGN_CENTER, 2, textBOLD, "head", "");
				String ownerDetails = "Name: " + invoice.getOwnerName() + "\n\nAddress: " + invoice.getAddress()
						+ "\nGSTIN: " + invoice.getGstin();
				insertTableCell(table1, ownerDetails, Element.ALIGN_LEFT, 1, dataFont, "first", "");
				String invDetails = "Invoice No: " + invoice.getInvNum() + "\nInvoice date: " + invoice.getInvDate()
						+ "\nReverse Charge (Y/N): N";
				insertTableCell(table1, invDetails, Element.ALIGN_LEFT, 1, dataFont, "middle", "");
				insertTableCell(table1, "", Element.ALIGN_CENTER, 2, headerFont, "head", "");
				document.add(table1);

				float[] columnWidths = { 15f, 45f, 20f, 15f, 15f, 15f, 35f };
				PdfPTable table2 = new PdfPTable(columnWidths);
				table2.setWidthPercentage(100);
				table2.setHorizontalAlignment(Element.ALIGN_CENTER);
				insertTableCell(table2, "S. No.", Element.ALIGN_CENTER, 1, textBOLD, "first", " #A9A9A9");
				insertTableCell(table2, "Product Description", Element.ALIGN_CENTER, 1, textBOLD, "first", " #A9A9A9");
				insertTableCell(table2, "SAC Code", Element.ALIGN_CENTER, 1, textBOLD, "first", " #A9A9A9");
				insertTableCell(table2, "UOM", Element.ALIGN_CENTER, 1, textBOLD, "first", " #A9A9A9");
				insertTableCell(table2, "Qty", Element.ALIGN_CENTER, 1, textBOLD, "first", " #A9A9A9");
				insertTableCell(table2, "Rate", Element.ALIGN_CENTER, 1, textBOLD, "first", " #A9A9A9");
				insertTableCell(table2, "Taxable Value", Element.ALIGN_CENTER, 1, textBOLD, "first", " #A9A9A9");
				insertTableCell(table2, "1", Element.ALIGN_CENTER, 1, dataFont, "first", "");
				insertTableCell(table2,
						"Facilitation Fee for " + invoice.getSite() + "\n\nSAC Code 997221\nCGST@9%\nSGST@9%\nIGST@18%",
						Element.ALIGN_LEFT, 1, dataFont, "first", "");
				insertTableCell(table2, "997221", Element.ALIGN_CENTER, 1, dataFont, "first", "");
				insertTableCell(table2, "SFT", Element.ALIGN_CENTER, 1, dataFont, "first", "");
				insertTableCell(table2, invoice.getQty() + "", Element.ALIGN_CENTER, 1, dataFont, "first", "");
				insertTableCell(table2, invoice.getRate() + "", Element.ALIGN_CENTER, 1, dataFont, "first", "");
				insertTableCell(table2, invoice.getTxVal() + "\n\n\n" + invoice.getCgst() + "" + "\n"
						+ invoice.getSgst() + "" + "\n" + invoice.getIgst() + "", Element.ALIGN_RIGHT, 1, dataFont,
						"first", "");
				insertTableCell(table2, "Total", Element.ALIGN_CENTER, 6, headerFont, "head", "");
				insertTableCell(table2, invoice.getTotalVal() + "", Element.ALIGN_RIGHT, 1, dataFont, "first", "");
				insertTableCell(table2,
						"AMOUNT IN WORDS -- "
								+ this.numberToWords(Integer.parseInt(df.format(invoice.getTotalVal()))).toUpperCase(),
						Element.ALIGN_CENTER, 7, textBOLD, "head", "");
				document.add(table2);

				PdfPTable table3 = new PdfPTable(2);
				table3.setWidthPercentage(100);
				table3.setHorizontalAlignment(Element.ALIGN_CENTER);
				String addr = "Bank A/C No: 62021746180, State Bank of India, IFB Branch, Somajiguda, Hyderabad Bank \nIFSC: SBIN0009103";
				insertTableCell(table3, addr, Element.ALIGN_LEFT, 1, textBOLD, "right", "");
				String ss = "Ceritified that the particulars given above are true and correct";
				insertTableCell(table3, ss, Element.ALIGN_LEFT, 1, dataFont, "bottom", "");
				insertTableCell(table3, "Terms & conditions", Element.ALIGN_CENTER, 1, textBOLD, "bottom-right", "");
				String nn = "For Meenakshi Infrastructures Pvt.Ltd\nAuthorised signatory";
				insertTableCell(table3, nn, Element.ALIGN_CENTER, 1, textBOLD, "top-bottom", "");

				ClassPathResource resource2 = new ClassPathResource("/images/Stamp.bmp");
				Image image2 = Image.getInstance(resource2.getFile().getPath());
				image2.scaleAbsolute(90, 90);
				image2.setAlignment(Image.MIDDLE);
				cellOne = new PdfPCell(image2);
				cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellOne.setPaddingTop(5f);
				cellOne.setPaddingBottom(5f);
				cellOne.setBorderWidthTop(0f);
				cellOne.setBorderWidthRight(0f);
				table3.addCell(cellOne);

				ClassPathResource resource3 = new ClassPathResource("/images/Signature.bmp");
				Image image3 = Image.getInstance(resource3.getFile().getPath());
				image3.scaleAbsolute(80, 50);
				image3.setAlignment(Image.MIDDLE);
				cellOne = new PdfPCell(image3);
				cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellOne.setPaddingTop(5f);
				cellOne.setPaddingBottom(5f);
				cellOne.setBorderWidthTop(0f);
				table3.addCell(cellOne);
				document.add(table3);
				document.close();
				String date = new SimpleDateFormat("E_MMM_dd_HH_m_ss_z_yyyy").format(new Date());
				String fileName = invoice.getId() + "." + invoice.getOwnerName() + "_" + invoice.getSite() + "_"
						+ invoice.getType() + "_" + invoice.getMonth() + date;
				System.out.println(
						"------------> PDF Generation Completed for " + invoice.getOwnerName() + " <-------------");
				multiPdfSource.add(new ByteArrayInputStream(out.toByteArray()));
				SendEmailRequest request = new SendEmailRequest();
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
				bulkMail.setInvoiceData(invoice);
				mailService.sendEmailWithAttachment(bulkMail);
			} catch (Exception e) {
				System.out.println("------------> PDF Generation Issue With Client  " + invoice.getOwnerName()
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
		if (position.equals("right"))
			cell.setBorderWidthRight(0);
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
		cell.setPaddingBottom(2f);
		table.addCell(cell);
	}

	private String convertLessThanOneThousand(int number) {
		String current;
		String[] tensNames = { "", " ten", " twenty", " thirty", " forty", " fifty", " sixty", " seventy", " eighty",
				" ninety" };

		String[] numNames = { "", " one", " two", " three", " four", " five", " six", " seven", " eight", " nine",
				" ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen", " seventeen",
				" eighteen", " nineteen" };
		if (number % 100 < 20) {
			current = numNames[number % 100];
			number /= 100;
		} else {
			current = numNames[number % 10];
			number /= 10;

			current = tensNames[number % 10] + current;
			number /= 10;
		}
		if (number == 0)
			return current;
		return numNames[number] + " hundred" + current;
	}

	public String numberToWords(int number) {

		String[] specialNames = { "", " thousand", " million", " billion", " trillion", " quadrillion",
				" quintillion" };

		if (number == 0) {
			return "zero";
		}

		String prefix = "";

		if (number < 0) {
			number = -number;
			prefix = "negative";
		}

		String current = "";
		int place = 0;

		do {
			int n = number % 1000;
			if (n != 0) {
				String s = convertLessThanOneThousand(n);
				current = s + specialNames[place] + current;
			}
			place++;
			number /= 1000;
		} while (number > 0);

		return (prefix + current).trim() + " rupess only.";
	}

	@Override
	public void downloadTemplate(HttpServletResponse response, String orgName) {
		try {
			System.out.println("------------> Template Downloading Start <-------------");
			String fileName = "";
			if(orgName.equalsIgnoreCase("planb"))
				fileName = "planb_template";
			else if(orgName.equalsIgnoreCase("meenakshiEinvoice"))
				fileName = "menakshi_einvoice_template";
			else
				fileName = "meenakshi_template";
			String extenstion = ".xlsx";
			String date = new SimpleDateFormat("E_MMM_dd_HH_m_ss_z_yyyy").format(new Date());
			ClassPathResource resource = new ClassPathResource("/images/" + fileName + extenstion);
			byte[] fileDataBytes = Util.getFileByteContent(resource.getFile().getAbsoluteFile());
			response.setContentType("application/anx.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + date + extenstion);
			FileCopyUtils.copy(fileDataBytes, response.getOutputStream());
			System.out.println("------------> Template Downloading End <-------------");
		} catch (Exception e) {
			System.out.println("------------> Template Downloading ERROR Found ISSUE <-------------");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void allPdfDocs(List<InputStream> multiplePdf, String folderName) {
		try {
			System.out.println("------------> OverallInvoices PDF Generation Start <-------------");
			PDFMergerUtility ut = new PDFMergerUtility();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ut.addSources(multiplePdf);
			String fileDestin = "F://" + folderName + "/" + "OverallInvoices" + ".pdf";
			ut.setDestinationFileName(fileDestin);
			ut.setDestinationStream(out);
			ut.mergeDocuments();
			try {
				FileOutputStream fos = new FileOutputStream(new File(fileDestin));
				fos.write(out.toByteArray());
				fos.flush();
				fos.close();
			} catch (Exception e) {
				FileOutputStream fos = new FileOutputStream(new File("F:\\OverallInvoices.pdf"));
				fos.write(out.toByteArray());
				fos.flush();
				fos.close();
			}
			SendEmailRequest request = new SendEmailRequest();
			request.setMailTo("rajesh@meenakshigroup.com,sskrajesh9@gmail.com,bhaskarkamma5@gmail.com");
			request.setSubject("Facilitation Invoices Mails Processing Inprogress");
			request.setContent("Facilitation Invoices Mails Processing Count '" + multiplePdf.size() + "'");
			request.setFileByteArray(out.toByteArray());
			request.setFileName("OverallInvoices");
			mailService.sendRegularEmail(request);
			System.out.println("------------> OverallInvoices PDF Generation Completed <-------------");
		} catch (Exception e) {
			System.out.println("------------> OverallInvoices PDF Generation Issue <-------------");
			System.out.println("------------> OverallInvoices PDF Issue '" + e.getMessage() + "' <-------------");
		}
	}

	
	public Map<String, byte[]> EinvoicePdfFileReading() {
		Map<String, byte[]> pdfDetailsMap = new HashMap<String, byte[]>();
		System.out.println("------------> PDF Files Reading Start  <-------------");
		try {
			File files = new File("F://PDFCOPIES");
			for (File file : files.listFiles()) {
				System.out.println("--------------" + file.getName() +"---------------");
				PDDocument document = PDDocument.load(file);
				PDFTextStripper pdfStripper = new PDFTextStripper();
				String einvoice = pdfStripper.getText(document);
				int count = 0;
				String docNum = "";
				for (String pdfData : einvoice.split(":")) {
					pdfData = pdfData.trim();
					if (pdfData.contains("Document No")){
						count = 1;
						continue;
					}
					if (count == 1) {
						docNum = pdfData.split(" ")[0].trim();
						count = 0;
						break;
					}
				}
				pdfDetailsMap.put(docNum, FileUtils.readFileToByteArray(file));
				System.out.println("------------> PDF File Reading Success For "+docNum+"  <-------------");
				document.close();
			}
			System.out.println("------------> PDF Files Reading END  <-------------");
		} catch (Exception e) {
			System.out.println("------------> PDF Files Reading ISSUE "+e.getMessage()+"  <-------------");
		}
		return pdfDetailsMap;
	}

	@SuppressWarnings("resource")
	@Override
	public ResponseModel ProcessEinvoiceMailExcelFile(MultipartFile file) {
		ResponseModel response = new ResponseModel();
		int currentRowNum = 0, currentColumnNum = 0;
		try {
			System.out.println("------------> EXCEL File Reading Started <-------------");
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			int numberOfSheets = workbook.getNumberOfSheets();
			List<MeenakshiInvsExcelData> invioceExcelList = new ArrayList<MeenakshiInvsExcelData>();
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
					MeenakshiInvsExcelData InvioceData = new MeenakshiInvsExcelData();
					currentRowNum = currentRow.getRowNum();
					while (cellsInRow.hasNext()) {
						Cell curCell = cellsInRow.next();
						isemptyCell = checkCellContainsDataOrNot(curCell);
						currentColumnNum = curCell.getColumnIndex();
						if (isemptyCell) {
							continue;
						}
						if (curCell.getColumnIndex() == 0)
							InvioceData.setId(((Double) curCell.getNumericCellValue()) != null
									? ((Double) curCell.getNumericCellValue()).intValue() + "" : 0.0 + "");

						if (curCell.getColumnIndex() == 1)
							InvioceData.setSite(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 2)
							InvioceData.setType(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");
						
						if (curCell.getColumnIndex() == 3)
							InvioceData.setMonth(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 4)
							InvioceData.setInvNum(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");
						
						if (curCell.getColumnIndex() == 5)
							InvioceData.setOwnerName(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");

						if (curCell.getColumnIndex() == 6)
							InvioceData.setMailTo(curCell.getStringCellValue() != null
									? !curCell.getStringCellValue().isEmpty() ? curCell.getStringCellValue() : "" : "");


					}
					if (!InvioceData.getInvNum().isEmpty()) {
						invioceExcelList.add(InvioceData);
						System.out.println("------------> EXCEL File Reading " + currentRow.getRowNum()
								+ " Row Completed <-------------");
					}
				}
			}
			System.out.println("------------> EXCEL File Reading Completed Successfully <-------------");
			response = this.EinvoicMailProcessing(invioceExcelList);
		} catch (Exception e) {
			System.out.println("------------> EXCEL File Reading Issue In " + currentRowNum + " Row Then Issue In "
					+ currentColumnNum + " Column <-------------");
			e.printStackTrace();
			response.setMessage("ERROR");
			response.setErrorFound(true);
		}
		return response;
	}
	
	public ResponseModel EinvoicMailProcessing(List<MeenakshiInvsExcelData> invioceExcelList) {
		ResponseModel response = new ResponseModel();
		try {
			Map<String, byte[]> pdfDetails = this.EinvoicePdfFileReading();
			String date = new SimpleDateFormat("E_MMM_dd_HH_m_ss_z_yyyy").format(new Date());
			String folder = "Meenakshi_Einvoice_Mails_"+date;
			File file = new File("F:\\" + folder);
			file.mkdir();
			for (MeenakshiInvsExcelData invoiceData : invioceExcelList) {
				if (pdfDetails.containsKey(invoiceData.getInvNum())) {
					System.out.println("------------> E-Invoices PDF Linked With Excel Data For "+invoiceData.getOwnerName()+" <-------------");
					SendEmailRequest request = new SendEmailRequest();
					request.setMailTo(invoiceData.getMailTo());
					request.setSubject("Facilitation E-Invoice for " + invoiceData.getMonth());
					request.setFileByteArray(pdfDetails.get(invoiceData.getInvNum()));					
					String fileName = invoiceData.getId() + "." + invoiceData.getOwnerName() + "_"
							+ invoiceData.getMonth() + date;
					request.setFileName(fileName);
					request.setCounterParty(invoiceData.getOwnerName());
					request.setFinPeriod(invoiceData.getMonth());
					BulkMailSendingModel bulkMailSender = new BulkMailSendingModel();
					bulkMailSender.setMailRequest(request);
					bulkMailSender.setInvoiceData(invoiceData);					
					bulkMailSender.setFolderName(folder);
					bulkMailSender.setFileName("Meenakshi_Einvoice_Mails_Info");
					mailService.sendEinvoiceEmailWithAttachment(bulkMailSender);
				} else {
					System.out.println("*****************************************************");
					System.out.println("*********----- E-Invoices PDF Not Linked With Excel Data For "+invoiceData.getOwnerName()+" -----********");
				}
			}
		} catch (Exception e) {
			response.setErrorFound(true);
			System.out.println("------------> E-Invoices PDF Linking Failed <-------------");
			System.out.println("------------> E-Invoices PDF Linking Issue '" + e.getMessage() + "' <-------------");
		}
		return response;
	}
}