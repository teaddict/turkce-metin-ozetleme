package com.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;

import com.webservice.model.*;
import com.webservice.service.*;


@RestController
public class WebController {
	private static final Logger LOGGER = Logger.getLogger( WebController.class.getName() );

	@Autowired
	SummaryService service;
	
//	@RequestMapping("/save")
//	public String process(){
//		repository.save(new Customer("Jack", "Smith"));
//		repository.save(new Customer("Adam", "Johnson"));
//		repository.save(new Customer("Kim", "Smith"));
//		repository.save(new Customer("David", "Williams"));
//		repository.save(new Customer("Peter", "Davis"));
//		return "Done";
//	}
//	
//	
//	@RequestMapping("/findall")
//	public String findAll(){
//		String result = "<html>";
//		
//		for(Customer cust : repository.findAll()){
//			result += "<div>" + cust.toString() + "</div>";
//		}
//		
//		return result + "</html>";
//	}
	
//	@RequestMapping("/findbyid")
//	public String findById(@RequestParam("id") long id){
//		String result = "";
//		result = repository.findOne(id).toString();
//		return result;
//	}
	@RequestMapping("/api")
	public Summary fetchDataByContext(@RequestParam("contextOfText") String contextOfText){
		LOGGER.info("WebController: " + contextOfText);
		Summary summary = service.add(contextOfText);
		return summary;
	}
    
    @CrossOrigin
	@RequestMapping(value = "/api/new", method = RequestMethod.POST)
	public @ResponseBody
	Summary getsummary(@RequestParam("contextOfText") String contextOfText) {
    	LOGGER.info("WebController: " + contextOfText);
		Summary summary = service.add(contextOfText);
		return summary;
	}
	
}
