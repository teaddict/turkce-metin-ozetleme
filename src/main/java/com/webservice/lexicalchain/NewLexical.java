package com.webservice.lexicalchain;
public class NewLexical {
	
	private String word;
	private int sentenceNo;
	private int paragraphNo;
	
	public NewLexical(String word, int sentenceNo, int paragraphNo) {
		this.word = word;
		this.sentenceNo = sentenceNo;
		this.paragraphNo = paragraphNo;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getSentenceNo() {
		return sentenceNo;
	}
	public void setSentenceNo(int sentenceNo) {
		this.sentenceNo = sentenceNo;
	}
	public int getParagraphNo() {
		return paragraphNo;
	}
	public void setParagraphNo(int paragraphNo) {
		this.paragraphNo = paragraphNo;
	}
	public void printLexical(){
		System.out.println("Word: "+this.word);
		System.out.println("Sentence No: " +this.sentenceNo);
		System.out.println("Paragraph No: "+this.paragraphNo);
	}
	

}
