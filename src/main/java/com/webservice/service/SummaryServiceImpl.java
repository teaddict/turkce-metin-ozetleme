package com.webservice.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webservice.controller.WebController;
import com.webservice.lexicalchain.NewPreprocess;
import com.webservice.model.Summary;
import com.webservice.repo.SummaryRepository;
@Service
public class SummaryServiceImpl implements SummaryService {
	private static final Logger LOGGER = Logger.getLogger( WebController.class.getName() );
	@Autowired
	private SummaryRepository summaryRepo;

	@Transactional
	public Summary add(String contextOfText) throws Exception {
		// TODO Auto-generated method stub
		Summary tempSummary = new Summary();
		tempSummary = summaryRepo.findBycontextOfText(contextOfText);
		if (tempSummary == null) {
			LOGGER.info("summary doesnt found");
			NewPreprocess preprocess = new NewPreprocess();
			tempSummary = new Summary();
			tempSummary.setContextOfText(contextOfText);
			tempSummary = preprocess.getSummary(tempSummary);
			summaryRepo.save(tempSummary);
			return tempSummary;
		} else {
			LOGGER.info("summary found in db");
			return tempSummary;
		}

	}

//	@Transactional
//	public void edit(Summary summary) {
//		// TODO Auto-generated method stub
//		summaryRepo.edit(summary);
//	}
//
//	@Transactional
//	public void delete(int summaryId) {
//		// TODO Auto-generated method stub
//		summaryRepo.delete(summaryId);
//	}
//
//	@Transactional
//	public Summary getSummary(int summaryId) {
//		// TODO Auto-generated method stub
//		return summaryRepo.getSummary(summaryId);
//	}
//
//	@Transactional
//	public List getAllSummary() {
//		// TODO Auto-generated method stub
//		return summaryRepo.getAllSummary();
//	}

}