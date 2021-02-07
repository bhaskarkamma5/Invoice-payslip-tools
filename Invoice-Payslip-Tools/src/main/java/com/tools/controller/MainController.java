package com.tools.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tools.models.ResponseModel;
import com.tools.services.MenkshiFileProcessingService;
import com.tools.services.ToolsProcessingService;

@Controller
public class MainController {
	
	@Autowired
	private MenkshiFileProcessingService fileProcessingService;
	
	@Autowired
	private ToolsProcessingService toolsProcessingService;
	
    @GetMapping("/")
    public String index() {
        return "jsp-views/login";
    }
    
    @RequestMapping(value="/login",method=RequestMethod.POST)
	public ModelAndView saveEmployee(@RequestBody Object request)
	{
    	ResponseModel response = toolsProcessingService.userLogin(request);
    	ModelAndView mav = new ModelAndView();
    	mav.addObject("msg", response.getMessage());
    	mav.setViewName("jsp-views/responseInfo");
    	if(!response.getMessageType().equalsIgnoreCase("SUCCESS"))
    		mav.setStatus(HttpStatus.NOT_ACCEPTABLE);
		return mav;
	}
    
    @GetMapping("/navigate/{pathName}")
    public String uploadform(@PathVariable("pathName") String path) {
        return "jsp-views/"+path;
    }
    
    @GetMapping("/dwnldtemplate/{orgName}")
	public void downloadTemplate(HttpServletResponse response, @PathVariable("orgName") String orgName) {
		fileProcessingService.downloadTemplate(response, orgName);
	}
}