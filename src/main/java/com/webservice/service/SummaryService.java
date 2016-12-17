package com.webservice.service;

import com.webservice.model.Summary;

public interface SummaryService {
	public Summary add(String contextOfText) throws Exception;
	public String getVerbs(String contextOfText) throws Exception;
	public String getNouns(String contextOfText) throws Exception;
	public String getClass(String contextOfText) throws Exception;

//	public void edit(Summary summary);
//	public void delete(int summaryId);
//	public Summary getSummary(int summaryId);
//	public List getAllSummary();
}


