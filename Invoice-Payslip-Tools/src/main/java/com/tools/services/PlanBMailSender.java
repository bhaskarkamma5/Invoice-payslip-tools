package com.tools.services;

import com.tools.models.BulkMailSendingModel;
import com.tools.models.ResponseModel;
import com.tools.models.SendEmailRequest;

public interface PlanBMailSender {
	
	ResponseModel sendEmailWithAttachment(BulkMailSendingModel bulkMailSender);
	
	ResponseModel sendRegularEmail(SendEmailRequest sndEmailRequest);
}
