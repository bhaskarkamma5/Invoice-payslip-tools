package com.tools.services;

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
}
