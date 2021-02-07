package com.tools.services;

import com.tools.models.BulkMailSendingModel;
import com.tools.models.ResponseModel;
import com.tools.models.SendEmailRequest;

public interface MenkshiMailSenderService {

	ResponseModel sendEmailWithAttachment(BulkMailSendingModel bulkMailSender);
	
	ResponseModel sendRegularEmail(SendEmailRequest sndEmailRequest);
	
	ResponseModel sendEinvoiceEmailWithAttachment(BulkMailSendingModel bulkMailSender);
}
