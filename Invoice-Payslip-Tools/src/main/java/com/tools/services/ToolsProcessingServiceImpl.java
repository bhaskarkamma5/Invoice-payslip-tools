package com.tools.services;

import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tools.models.ResponseModel;

@Service
public class ToolsProcessingServiceImpl implements ToolsProcessingService {

	@Autowired
	private ServletContext servletContext;

	@Override
	public ResponseModel userLogin(Object data) {
		ResponseModel response = new ResponseModel();
		try {
			String userName = servletContext.getInitParameter("userName");
			String pswd = servletContext.getInitParameter("password");
			Map userData = (Map) data;
			if(userData.containsKey("username")){
				String msg = userName.equalsIgnoreCase(userData.get("username")+"") ? "SUCCESS" : "Invalid UserName";
				response.setMessage(msg);
				if(!msg.equalsIgnoreCase("SUCCESS")){
					response.setMessageType("ERROR");
					return response;
				}
			}
			if(userData.containsKey("pswd")){
				String msg = pswd.equalsIgnoreCase(userData.get("pswd")+"") ? "SUCCESS" : "Invalid Password";
				response.setMessage(msg);
				if(!msg.equalsIgnoreCase("SUCCESS")){
					response.setMessageType("ERROR");
					return response;
				}
			}
			response.setMessageType("SUCCESS");
			response.setMessage("Logged In Successfully");
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
		
}
