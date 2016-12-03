package com.webservice.service;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webservice.controller.WebController;
import com.webservice.lexicalchain.NewPreprocess;
import com.webservice.model.Summary;
import com.webservice.repo.SummaryRepository;
@Service
public class SummaryServiceImpl implements SummaryService {
	private static final Logger LOGGER = Logger.getLogger( WebController.class.getName() );

	private final SummaryRepository summaryRepo;

    @Inject
    public SummaryServiceImpl(final SummaryRepository summaryRepo) {
        this.summaryRepo = summaryRepo;
    }
	@Transactional
	public Summary add(String contextOfText) {
		// TODO Auto-generated method stub
		Summary tempSummary = new Summary();
		Summary newSummary = new Summary();
		tempSummary.setContextOfText(contextOfText);
		newSummary.setContextOfText(contextOfText);
		try {
			tempSummary = summaryRepo.findBycontextOfText(contextOfText);
			if (tempSummary == null) {
				LOGGER.info("summary doesnt found");
				NewPreprocess preprocess = new NewPreprocess();
				newSummary = preprocess.getSummary(newSummary);
				summaryRepo.save(newSummary);
				LOGGER.info("new summary saved");
			}else{
				newSummary = tempSummary;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newSummary;
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