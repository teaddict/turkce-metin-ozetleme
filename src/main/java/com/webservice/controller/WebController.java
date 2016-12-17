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
	
	@RequestMapping("/api")
	public Summary fetchDataByContext(@RequestParam("contextOfText") String contextOfText) throws Exception{
		LOGGER.info("WebController: " + contextOfText);
		Summary summary = service.add(contextOfText);
		return summary;
	}
    
    @CrossOrigin
	@RequestMapping(value = "/api/new", method = RequestMethod.POST)
	public @ResponseBody
	Summary getSummary(@RequestParam("contextOfText") String contextOfText) throws Exception {
    	LOGGER.info("WebController: " + contextOfText);
		Summary summary = service.add(contextOfText);
		return summary;
	}
    
    @CrossOrigin
	@RequestMapping(value = "/api/verb/new", method = RequestMethod.POST)
	public @ResponseBody
	String getVerbs(@RequestParam("contextOfText") String contextOfText) throws Exception {
    	LOGGER.info("WebController GetVerbs: " + contextOfText);
		return service.getVerbs(contextOfText);
	}
    
    @CrossOrigin
	@RequestMapping(value = "/api/noun/new", method = RequestMethod.POST)
	public @ResponseBody
	String getNouns(@RequestParam("contextOfText") String contextOfText) throws Exception {
    	LOGGER.info("WebController GetNouns: " + contextOfText);
		return service.getNouns(contextOfText);
	}
    
    @CrossOrigin
	@RequestMapping(value = "/api/class/new", method = RequestMethod.POST)
	public @ResponseBody
	String getClass(@RequestParam("contextOfText") String contextOfText) throws Exception {
    	LOGGER.info("WebController GetClass: " + contextOfText);
		return service.getClass(contextOfText);
	}
	
}
