package com.tools.services;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.tools.models.ResponseModel;

public interface MenkshiFileProcessingService {

	ResponseModel ProcessExcelFile(MultipartFile file);
	
	void downloadTemplate(HttpServletResponse response, String orgName);
	
	ResponseModel ProcessEinvoiceMailExcelFile(MultipartFile file);
}
