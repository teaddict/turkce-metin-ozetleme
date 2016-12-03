package com.webservice.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "summary")
public class Summary implements Serializable {

	private static final long serialVersionUID = -3009157732242241606L;
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int summaryId;
	@Column(name = "context_of_text")
	private String contextOfText;
	@Column(name = "summary_of_text")
	private String summaryOfText;
	@Column(name = "word_chain")
	private String wordChain;
	@Column(name = "filename")
	private String filename;
	@Column(name = "class_of_text")
	private String classOfText;
	@Column(name = "class_of_summary")
	private String classOfSummary;

	public Summary() {
	}

	public Summary(int summaryId, String contextOfText, String summaryOfText, String wordChain, String filename,
			String classOfText, String classOfSummary) {
		super();
		this.summaryId = summaryId;
		this.contextOfText = contextOfText;
		this.summaryOfText = summaryOfText;
		this.wordChain = wordChain;
		this.filename = filename;
		this.classOfText = classOfText;
		this.classOfSummary = classOfSummary;
	}

	public int getSummaryId() {
		return summaryId;
	}

	public void setSummaryId(int summaryId) {
		this.summaryId = summaryId;
	}

	public String getContextOfText() {
		return contextOfText;
	}

	public void setContextOfText(String contextOfText) {
		this.contextOfText = contextOfText;
	}

	public String getSummaryOfText() {
		return summaryOfText;
	}

	public void setSummaryOfText(String summaryOfText) {
		this.summaryOfText = summaryOfText;
	}

	public String getWordChain() {
		return wordChain;
	}

	public void setWordChain(String wordChain) {
		this.wordChain = wordChain;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getClassOfText() {
		return classOfText;
	}

	public void setClassOfText(String classOfText) {
		this.classOfText = classOfText;
	}

	public String getClassOfSummary() {
		return classOfSummary;
	}

	public void setClassOfSummary(String classOfSummary) {
		this.classOfSummary = classOfSummary;
	}

}