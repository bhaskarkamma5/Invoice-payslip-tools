package com.tools.services;

import org.springframework.web.multipart.MultipartFile;

import com.tools.models.ResponseModel;

public interface PlanBFileProcessingService {

	ResponseModel ProcessPlanBExcelFile(MultipartFile file);
}
