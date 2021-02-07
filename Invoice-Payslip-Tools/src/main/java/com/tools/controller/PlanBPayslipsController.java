package com.tools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tools.models.ResponseModel;
import com.tools.services.PlanBFileProcessingService;

@Controller
@RequestMapping("/navigate")
public class PlanBPayslipsController {
	@Autowired
	private PlanBFileProcessingService planBFileProcessingService;

	@PostMapping("/planbexcel")
	public String uploadMultipartFile(@RequestParam("uploadfile") MultipartFile file, Model model) {
		try {
			System.out.println("<--------------------------------------------->");
			ResponseModel response = planBFileProcessingService.ProcessPlanBExcelFile(file);
			model.addAttribute("message", "File uploaded successfully!");
			System.out.println("<--------------------------------------------->");
			if(response.isErrorFound())
				return "jsp-views/mailFail";
		} catch (Exception e) {
			model.addAttribute("message", "Fail! -> uploaded filename: " + file.getOriginalFilename());
			System.out.println("<------------------------ "+ e.getMessage() +" -------------------->");
			return "jsp-views/mailFail";
		}
		return "jsp-views/mailFail";
	}
}
