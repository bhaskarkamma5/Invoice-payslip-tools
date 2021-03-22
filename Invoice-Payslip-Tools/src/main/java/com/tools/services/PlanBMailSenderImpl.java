package com.tools.services;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tools.models.BulkMailSendingModel;
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
		ResponseModel response = new ResponseModel();
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
			}/* else
				this.processingExcelForMissingMails(bulkMailSender.getInvoiceData(), bulkMailSender.getFolderName(),
						bulkMailSender.getFileName());*/
		} catch (Exception e) {
			System.out.println("------------> MAIL SENT ISSUE FOR CLIENT '" + sndEmailRequest.getCounterParty()
					+ "' <-------------");
			System.out.println("------------> MAIL SENT ISSUE  '" + e.getMessage() + "' <-------------");
			response.setErrorFound(true);
			//this.processingExcelForMissingMails(bulkMailSender.getInvoiceData(), bulkMailSender.getFolderName(),
				//	bulkMailSender.getFileName());
		}
		return response;
	
	}
}
